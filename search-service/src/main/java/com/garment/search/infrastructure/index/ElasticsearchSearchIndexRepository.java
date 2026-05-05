package com.garment.search.infrastructure.index;

import com.garment.search.domain.index.model.IndexType;
import com.garment.search.domain.index.model.SearchDocument;
import com.garment.search.domain.index.repository.SearchIndexRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于 Elasticsearch 的索引仓储实现。
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ElasticsearchSearchIndexRepository implements SearchIndexRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    private static final String INDEX_PREFIX = "garment_";

    @Override
    public void index(SearchDocument document) {
        try {
            Map<String, Object> source = buildSource(document);
            String indexName = resolveIndexName(document.getIndexType(), document.getTenantId());

            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withId(document.getId())
                    .withObject(source)
                    .build();

            elasticsearchOperations.index(indexQuery, IndexCoordinates.of(indexName));
            log.debug("索引文档成功: index={}, id={}", indexName, document.getId());
        } catch (Exception e) {
            log.error("索引文档失败: type={}, id={}", document.getIndexType(), document.getId(), e);
            throw new RuntimeException("索引文档失败", e);
        }
    }

    @Override
    public void bulkIndex(List<SearchDocument> documents) {
        try {
            for (SearchDocument document : documents) {
                index(document);
            }
            log.debug("批量索引成功: count={}", documents.size());
        } catch (Exception e) {
            log.error("批量索引失败: count={}", documents.size(), e);
            throw new RuntimeException("批量索引失败", e);
        }
    }

    @Override
    public void delete(String indexType, String id) {
        try {
            elasticsearchOperations.delete(id, IndexCoordinates.of(INDEX_PREFIX + indexType + "*"));
            log.debug("删除文档成功: indexType={}, id={}", indexType, id);
        } catch (Exception e) {
            log.error("删除文档失败: indexType={}, id={}", indexType, id, e);
            throw new RuntimeException("删除文档失败", e);
        }
    }

    @Override
    public void update(SearchDocument document) {
        // 使用 index 的 upsert 语义实现更新
        index(document);
        log.debug("更新文档成功: type={}, id={}", document.getIndexType(), document.getId());
    }

    private Map<String, Object> buildSource(SearchDocument document) {
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

    private String resolveIndexName(IndexType indexType, Long tenantId) {
        return INDEX_PREFIX + indexType.getCode() + "_" + tenantId;
    }
}
