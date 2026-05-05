package com.garment.search.infrastructure.search;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garment.search.domain.index.model.IndexType;
import com.garment.search.domain.index.model.SearchDocument;
import com.garment.search.domain.index.model.SearchResult;
import com.garment.search.domain.index.model.SuggestResult;
import com.garment.search.domain.index.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 基于 Elasticsearch 的搜索仓储实现。
 * <p>
 * 使用 Spring Data Elasticsearch 的 ElasticsearchOperations 实现搜索索引的增删查。
 * 索引命名规则：{prefix}{tenantId}_{indexType}，例如 garment_1_style。
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ElasticsearchSearchRepository implements SearchRepository {

    private final ElasticsearchOperations elasticsearchOperations;
    private final ObjectMapper objectMapper;

    @Value("${search.index.prefix:garment_}")
    private String indexPrefix;

    @Override
    public void index(SearchDocument document) {
        String indexName = resolveIndexName(document.getTenantId(), document.getIndexType());
        Map<String, Object> source = buildDocumentSource(document);

        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(document.getId())
                .withObject(source)
                .build();

        elasticsearchOperations.index(indexQuery, IndexCoordinates.of(indexName));
        log.debug("索引文档完成: index={}, id={}", indexName, document.getId());
    }

    @Override
    public void bulkIndex(List<SearchDocument> documents) {
        if (documents == null || documents.isEmpty()) {
            return;
        }

        Map<String, List<SearchDocument>> grouped = documents.stream()
                .collect(Collectors.groupingBy(d -> resolveIndexName(d.getTenantId(), d.getIndexType())));

        for (Map.Entry<String, List<SearchDocument>> entry : grouped.entrySet()) {
            String indexName = entry.getKey();
            List<IndexQuery> queries = entry.getValue().stream()
                    .map(doc -> new IndexQueryBuilder()
                            .withId(doc.getId())
                            .withObject(buildDocumentSource(doc))
                            .build())
                    .toList();

            elasticsearchOperations.bulkIndex(queries, IndexCoordinates.of(indexName));
            log.debug("批量索引完成: index={}, count={}", indexName, queries.size());
        }
    }

    @Override
    public void delete(String indexType, String id) {
        try {
            elasticsearchOperations.delete(id, IndexCoordinates.of(indexType));
        } catch (Exception e) {
            log.warn("删除文档失败: indexType={}, id={}, error={}", indexType, id, e.getMessage());
        }
    }

    @Override
    public SearchResult search(String keyword, List<String> indexTypes,
                               Map<String, Object> filters, String sortField,
                               boolean sortDesc, int page, int size) {
        long startTime = System.currentTimeMillis();
        Long tenantId = com.garment.common.domain.AuthUserContext.requireTenantId();

        List<String> indexNames = resolveSearchIndexes(tenantId, indexTypes);

        NativeQueryBuilder queryBuilder = new NativeQueryBuilder();

        if (keyword != null && !keyword.isBlank()) {
            queryBuilder.withQuery(Query.of(q -> q.multiMatch(m -> m
                    .fields(List.of("title", "body", "title.ik", "body.ik"))
                    .query(keyword))));
        } else {
            queryBuilder.withQuery(Query.of(q -> q.matchAll(m -> m)));
        }

        if (filters != null && !filters.isEmpty()) {
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();
                queryBuilder.withFilter(Query.of(q -> q.term(t -> t
                        .field(field)
                        .value(String.valueOf(value)))));
            }
        }

        queryBuilder.withPageable(PageRequest.of(page, size));

        if (sortField != null && !sortField.isBlank()) {
            Sort sort = sortDesc
                    ? Sort.by(Sort.Order.desc(sortField))
                    : Sort.by(Sort.Order.asc(sortField));
            queryBuilder.withSort(sort);
        }

        NativeQuery searchQuery = queryBuilder.build();

        SearchHits<Map> searchHits = elasticsearchOperations.search(
                searchQuery, Map.class, IndexCoordinates.of(indexNames.toArray(new String[0])));

        List<SearchDocument> documents = searchHits.getSearchHits().stream()
                .map(this::hitToDocument)
                .toList();

        long tookMs = System.currentTimeMillis() - startTime;

        return SearchResult.builder()
                .documents(documents)
                .total(searchHits.getTotalHits())
                .page(page)
                .size(size)
                .tookMs(tookMs)
                .build();
    }

    @Override
    public List<SuggestResult> suggest(String keyword, String indexType) {
        Long tenantId = com.garment.common.domain.AuthUserContext.requireTenantId();
        List<String> indexNames;
        if (indexType != null && !indexType.isBlank()) {
            indexNames = List.of(resolveIndexName(tenantId, IndexType.fromCode(indexType)));
        } else {
            indexNames = resolveSearchIndexes(tenantId, null);
        }

        NativeQuery query = new NativeQueryBuilder()
                .withQuery(Query.of(q -> q.prefix(p -> p
                        .field("title.keyword")
                        .value(keyword.toLowerCase()))))
                .withPageable(PageRequest.of(0, 10))
                .build();

        SearchHits<Map> hits = elasticsearchOperations.search(
                query, Map.class, IndexCoordinates.of(indexNames.toArray(new String[0])));

        return hits.getSearchHits().stream()
                .map(hit -> {
                    Map<String, Object> source = hit.getContent();
                    return SuggestResult.builder()
                            .text((String) source.get("title"))
                            .indexType((String) source.get("indexType"))
                            .score(hit.getScore())
                            .build();
                })
                .toList();
    }

    // ============ 私有辅助方法 ============

    private String resolveIndexName(Long tenantId, IndexType indexType) {
        return indexPrefix + tenantId + "_" + indexType.getCode();
    }

    private List<String> resolveSearchIndexes(Long tenantId, List<String> indexTypes) {
        if (indexTypes != null && !indexTypes.isEmpty()) {
            return indexTypes.stream()
                    .map(code -> resolveIndexName(tenantId, IndexType.fromCode(code)))
                    .toList();
        }
        return Arrays.stream(IndexType.values())
                .map(type -> resolveIndexName(tenantId, type))
                .toList();
    }

    private Map<String, Object> buildDocumentSource(SearchDocument document) {
        Map<String, Object> source = new HashMap<>();
        source.put("id", document.getId());
        source.put("indexType", document.getIndexType().getCode());
        source.put("tenantId", document.getTenantId());
        source.put("title", document.getTitle());
        source.put("body", document.getBody());
        source.put("createdAt", document.getCreatedAt() != null ? document.getCreatedAt().toString() : null);
        source.put("updatedAt", document.getUpdatedAt() != null ? document.getUpdatedAt().toString() : null);
        if (document.getAttributes() != null) {
            source.putAll(document.getAttributes());
        }
        return source;
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
                .indexType(source.get("indexType") != null ? IndexType.fromCode((String) source.get("indexType")) : null)
                .tenantId(source.get("tenantId") != null ? Long.valueOf(source.get("tenantId").toString()) : null)
                .title((String) source.get("title"))
                .body((String) source.get("body"))
                .attributes(attributes.isEmpty() ? null : attributes)
                .build();
    }
}
