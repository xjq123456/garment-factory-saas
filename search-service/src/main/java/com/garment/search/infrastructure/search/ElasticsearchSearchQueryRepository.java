package com.garment.search.infrastructure.search;

import com.garment.search.domain.index.model.IndexType;
import com.garment.search.domain.index.model.SearchDocument;
import com.garment.search.domain.query.model.SearchQuery;
import com.garment.search.domain.query.model.SearchResult;
import com.garment.search.domain.query.repository.SearchQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基于 Elasticsearch 的搜索查询仓储实现。
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ElasticsearchSearchQueryRepository implements SearchQueryRepository {

    private final RestHighLevelClient client;

    private static final String INDEX_PREFIX = "garment_";

    @Override
    public SearchResult search(SearchQuery query) {
        try {
            SearchRequest searchRequest = buildSearchRequest(query);
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            List<SearchDocument> documents = extractDocuments(response);
            long totalHits = response.getHits().getTotalHits() != null
                    ? response.getHits().getTotalHits().value : 0;
            int totalPages = (int) Math.ceil((double) totalHits / query.getSize());

            return SearchResult.builder()
                    .documents(documents)
                    .totalHits(totalHits)
                    .page(query.getPage())
                    .size(query.getSize())
                    .totalPages(totalPages)
                    .build();
        } catch (Exception e) {
            log.error("搜索失败: keyword={}, tenantId={}", query.getKeyword(), query.getTenantId(), e);
            throw new RuntimeException("搜索失败", e);
        }
    }

    /**
     * 构建 ES 搜索请求。
     */
    private SearchRequest buildSearchRequest(SearchQuery query) {
        // 确定搜索的索引列表
        String[] indices = resolveIndices(query);

        SearchRequest searchRequest = new SearchRequest(indices);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 构建查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // 全文搜索：匹配 title 和 body
        if (query.getKeyword() != null && !query.getKeyword().isBlank()) {
            MultiMatchQueryBuilder multiMatch = QueryBuilders.multiMatchQuery(
                            query.getKeyword(), "title^3", "body", "title.ik^2")
                    .type(MultiMatchQueryBuilder.Type.BEST_FIELDS)
                    .fuzziness("AUTO");
            boolQuery.must(multiMatch);
        } else {
            boolQuery.must(QueryBuilders.matchAllQuery());
        }

        // 租户过滤
        if (query.getTenantId() != null) {
            boolQuery.filter(QueryBuilders.termQuery("tenantId", query.getTenantId()));
        }

        // 索引类型过滤
        if (query.getIndexTypes() != null && !query.getIndexTypes().isEmpty()) {
            List<String> typeNames = query.getIndexTypes().stream()
                    .map(IndexType::name)
                    .collect(Collectors.toList());
            boolQuery.filter(QueryBuilders.termsQuery("indexType", typeNames));
        }

        // 自定义过滤条件
        if (query.getFilters() != null) {
            for (Map.Entry<String, Object> entry : query.getFilters().entrySet()) {
                boolQuery.filter(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
            }
        }

        sourceBuilder.query(boolQuery);

        // 分页
        sourceBuilder.from(query.getPage() * query.getSize());
        sourceBuilder.size(query.getSize());

        // 排序
        if (query.getSortField() != null && !query.getSortField().isBlank()) {
            SortOrder order = query.isSortDesc() ? SortOrder.DESC : SortOrder.ASC;
            sourceBuilder.sort(SortBuilders.fieldSort(query.getSortField()).order(order));
        } else if (query.getKeyword() != null && !query.getKeyword().isBlank()) {
            // 有关键词时按相关性排序（默认）
            sourceBuilder.sort("_score", SortOrder.DESC);
        } else {
            // 无关键词时按更新时间排序
            sourceBuilder.sort("updatedAt", SortOrder.DESC);
        }

        // 高亮
        sourceBuilder.highlighter(
                new org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder()
                        .field("title", 100, 1)
                        .field("body", 200, 1)
                        .preTags("<em>")
                        .postTags("</em>")
        );

        searchRequest.source(sourceBuilder);
        return searchRequest;
    }

    /**
     * 解析要搜索的索引名称列表。
     */
    private String[] resolveIndices(SearchQuery query) {
        if (query.getIndexTypes() != null && !query.getIndexTypes().isEmpty()) {
            return query.getIndexTypes().stream()
                    .map(type -> INDEX_PREFIX + type.name().toLowerCase() + "_" + query.getTenantId())
                    .toArray(String[]::new);
        }
        // 搜索所有索引
        return new String[]{INDEX_PREFIX + "*" + "_" + query.getTenantId()};
    }

    /**
     * 从 ES 响应中提取文档列表。
     */
    private List<SearchDocument> extractDocuments(SearchResponse response) {
        List<SearchDocument> documents = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> source = hit.getSourceAsMap();
            SearchDocument document = SearchDocument.builder()
                    .id((String) source.get("id"))
                    .indexType(parseIndexType((String) source.get("indexType")))
                    .tenantId(toLong(source.get("tenantId")))
                    .title((String) source.get("title"))
                    .body((String) source.get("body"))
                    .createdAt(parseDateTime((String) source.get("createdAt")))
                    .updatedAt(parseDateTime((String) source.get("updatedAt")))
                    .score(hit.getScore())
                    .attributes(extractAttributes(source))
                    .build();
            documents.add(document);
        }
        return documents;
    }

    /**
     * 提取非标准字段作为 attributes。
     */
    private Map<String, Object> extractAttributes(Map<String, Object> source) {
        Map<String, Object> attributes = new HashMap<>(source);
        attributes.remove("id");
        attributes.remove("indexType");
        attributes.remove("tenantId");
        attributes.remove("title");
        attributes.remove("body");
        attributes.remove("createdAt");
        attributes.remove("updatedAt");
        return attributes.isEmpty() ? null : attributes;
    }

    private IndexType parseIndexType(String value) {
        if (value == null) return null;
        try {
            return IndexType.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Long toLong(Object value) {
        if (value == null) return null;
        if (value instanceof Long) return (Long) value;
        if (value instanceof Number) return ((Number) value).longValue();
        return Long.parseLong(value.toString());
    }

    private LocalDateTime parseDateTime(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return LocalDateTime.parse(value);
        } catch (Exception e) {
            return null;
        }
    }
}