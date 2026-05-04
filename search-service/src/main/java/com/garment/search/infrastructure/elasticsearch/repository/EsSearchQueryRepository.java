package com.garment.search.infrastructure.elasticsearch.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.garment.search.domain.index.model.IndexType;
import com.garment.search.domain.query.model.SearchQuery;
import com.garment.search.domain.query.model.SearchResult;
import com.garment.search.domain.query.repository.SearchQueryRepository;
import com.garment.search.infrastructure.elasticsearch.entity.SearchDocumentEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基于 Elasticsearch 的搜索查询仓库实现。
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class EsSearchQueryRepository implements SearchQueryRepository {

    private final ElasticsearchClient client;

    private static final String INDEX_PREFIX = "garment_";

    @Override
    public SearchResult search(SearchQuery query) {
        try {
            List<String> indexNames = resolveIndexNames(query.getIndexTypes());

            SearchRequest.Builder requestBuilder = new SearchRequest.Builder()
                    .index(indexNames)
                    .from(query.getPage() * query.getSize())
                    .size(query.getSize());

            // 构建查询
            Query esQuery = buildEsQuery(query);
            requestBuilder.query(esQuery);

            // 高亮
            requestBuilder.highlight(h -> h
                    .fields("title", hf -> hf.preTags("<em>").postTags("</em>"))
                    .fields("body", hf -> hf.preTags("<em>").postTags("</em>")));

            // 排序
            if (query.getSortField() != null && !query.getSortField().isEmpty()) {
                SortOrder order = query.isSortDesc() ? SortOrder.Desc : SortOrder.Asc;
                requestBuilder.sort(s -> s.field(f -> f.field(query.getSortField()).order(order)));
            }

            // 过滤租户
            requestBuilder.postFilter(f -> f.term(t -> t.field("tenant_id").value(query.getTenantId())));

            SearchResponse<SearchDocumentEntity> response = client.search(requestBuilder.build(), SearchDocumentEntity.class);

            // 转换结果
            List<SearchResult.SearchHit> hits = response.hits().hits().stream()
                    .map(this::toSearchHit)
                    .collect(Collectors.toList());

            long totalHits = response.hits().total() != null ? response.hits().total().value() : 0;

            return SearchResult.builder()
                    .hits(hits)
                    .total(totalHits)
                    .page(query.getPage())
                    .size(query.getSize())
                    .tookMs(response.took())
                    .build();

        } catch (IOException e) {
            throw new RuntimeException("搜索查询失败: " + query.getKeyword(), e);
        }
    }

    /**
     * 解析索引类型列表为索引名称列表。
     */
    private List<String> resolveIndexNames(List<IndexType> indexTypes) {
        if (indexTypes == null || indexTypes.isEmpty()) {
            // 搜索所有业务索引
            return List.of(INDEX_PREFIX + "*");
        }
        return indexTypes.stream()
                .map(type -> INDEX_PREFIX + type.getCode())
                .collect(Collectors.toList());
    }

    /**
     * 构建 ES 查询语句。
     */
    private Query buildEsQuery(SearchQuery query) {
        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();

        // 关键词搜索：multi_match 在 title 和 body 上搜索
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            boolBuilder.must(m -> m
                    .multiMatch(mm -> mm
                            .fields("title^3", "body", "attributes.*")
                            .query(query.getKeyword())
                            .type(co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType.BestFields)
                            .fuzziness("AUTO")));
        }

        // 应用过滤条件
        if (query.getFilters() != null) {
            for (Map.Entry<String, Object> entry : query.getFilters().entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();
                boolBuilder.filter(f -> f.term(t -> t.field(field).value(String.valueOf(value))));
            }
        }

        return new Query.Builder().bool(boolBuilder.build()).build();
    }

    /**
     * 将 ES hit 转换为 SearchResult.SearchHit。
     */
    private SearchResult.SearchHit toSearchHit(Hit<SearchDocumentEntity> hit) {
        SearchDocumentEntity source = hit.source();
        Map<String, List<String>> highlights = new HashMap<>();

        if (hit.highlight() != null) {
            for (Map.Entry<String, List<String>> entry : hit.highlight().entrySet()) {
                highlights.put(entry.getKey(), entry.getValue());
            }
        }

        Map<String, Object> attributes = source != null ? source.getAttributes() : Map.of();

        return SearchResult.SearchHit.builder()
                .id(hit.id())
                .indexType(source != null ? source.getIndexType() : null)
                .title(source != null ? source.getTitle() : null)
                .body(source != null ? source.getBody() : null)
                .score(hit.score() != null ? hit.score() : 0.0)
                .highlights(highlights)
                .attributes(attributes)
                .build();
    }
}