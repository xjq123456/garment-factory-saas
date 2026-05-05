package com.garment.search.infrastructure.search;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.garment.search.domain.index.model.IndexType;
import com.garment.search.domain.index.model.SearchDocument;
import com.garment.search.domain.query.model.SearchQuery;
import com.garment.search.domain.query.model.SearchResult;
import com.garment.search.domain.query.repository.SearchQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于 Elasticsearch 的搜索查询仓储实现。
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ElasticsearchSearchQueryRepository implements SearchQueryRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    private static final String INDEX_PREFIX = "garment_";

    @Override
    public SearchResult search(SearchQuery query) {
        try {
            String[] indices = resolveIndices(query);

            NativeQueryBuilder queryBuilder = new NativeQueryBuilder();

            // 全文搜索
            if (query.getKeyword() != null && !query.getKeyword().isBlank()) {
                queryBuilder.withQuery(Query.of(q -> q.multiMatch(m -> m
                        .fields(List.of("title", "body", "title.ik"))
                        .query(query.getKeyword()))));
            } else {
                queryBuilder.withQuery(Query.of(q -> q.matchAll(m -> m)));
            }

            // 过滤条件
            if (query.getTenantId() != null) {
                queryBuilder.withFilter(Query.of(q -> q.term(t -> t
                        .field("tenantId")
                        .value(query.getTenantId()))));
            }
            if (query.getIndexTypes() != null && !query.getIndexTypes().isEmpty()) {
                List<String> typeNames = query.getIndexTypes().stream()
                        .map(IndexType::name)
                        .toList();
                queryBuilder.withFilter(Query.of(q -> q.terms(t -> t
                        .field("indexType")
                        .terms(tv -> tv.value(typeNames.stream()
                                .map(co.elastic.clients.elasticsearch._types.FieldValue::of)
                                .toList())))));
            }
            if (query.getFilters() != null) {
                for (Map.Entry<String, Object> entry : query.getFilters().entrySet()) {
                    String field = entry.getKey();
                    Object value = entry.getValue();
                    queryBuilder.withFilter(Query.of(q -> q.term(t -> t
                            .field(field)
                            .value(String.valueOf(value)))));
                }
            }

            // 分页
            queryBuilder.withPageable(PageRequest.of(query.getPage(), query.getSize()));

            // 排序
            if (query.getSortField() != null && !query.getSortField().isBlank()) {
                Sort sort = query.isSortDesc()
                        ? Sort.by(Sort.Order.desc(query.getSortField()))
                        : Sort.by(Sort.Order.asc(query.getSortField()));
                queryBuilder.withSort(sort);
            }

            NativeQuery searchQuery = queryBuilder.build();

            SearchHits<Map> hits = elasticsearchOperations.search(
                    searchQuery, Map.class, IndexCoordinates.of(indices));

            List<SearchDocument> documents = hits.getSearchHits().stream()
                    .map(this::hitToDocument)
                    .toList();

            long totalHits = hits.getTotalHits();
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

    private String[] resolveIndices(SearchQuery query) {
        if (query.getIndexTypes() != null && !query.getIndexTypes().isEmpty()) {
            return query.getIndexTypes().stream()
                    .map(type -> INDEX_PREFIX + type.name().toLowerCase() + "_" + query.getTenantId())
                    .toArray(String[]::new);
        }
        return new String[]{INDEX_PREFIX + "*" + "_" + query.getTenantId()};
    }

    private SearchDocument hitToDocument(SearchHit<Map> hit) {
        Map<String, Object> source = hit.getContent();
        Map<String, Object> attributes = new HashMap<>(source);
        attributes.remove("id");
        attributes.remove("indexType");
        attributes.remove("tenantId");
        attributes.remove("title");
        attributes.remove("body");
        attributes.remove("createdAt");
        attributes.remove("updatedAt");

        return SearchDocument.builder()
                .id((String) source.get("id"))
                .indexType(parseIndexType((String) source.get("indexType")))
                .tenantId(toLong(source.get("tenantId")))
                .title((String) source.get("title"))
                .body((String) source.get("body"))
                .createdAt(parseDateTime((String) source.get("createdAt")))
                .updatedAt(parseDateTime((String) source.get("updatedAt")))
                .attributes(attributes.isEmpty() ? null : attributes)
                .build();
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
