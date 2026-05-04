package com.garment.search.infrastructure.elasticsearch.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.garment.search.domain.index.model.IndexType;
import com.garment.search.domain.index.model.SearchDocument;
import com.garment.search.domain.index.repository.SearchIndexRepository;
import com.garment.search.infrastructure.elasticsearch.entity.SearchDocumentEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

/**
 * 基于 Elasticsearch 的搜索索引仓库实现。
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class EsSearchIndexRepository implements SearchIndexRepository {

    private final ElasticsearchClient client;

    /** 索引名称前缀，格式：garment_{indexType} */
    private static final String INDEX_PREFIX = "garment_";

    @Override
    public void index(SearchDocument document) {
        try {
            SearchDocumentEntity entity = toEntity(document);
            String indexName = buildIndexName(document.getIndexType());

            IndexRequest<SearchDocumentEntity> request = IndexRequest.of(b -> b
                    .index(indexName)
                    .id(document.getId())
                    .document(entity));

            client.index(request);
            log.info("文档索引成功: index={}, id={}", indexName, document.getId());
        } catch (IOException e) {
            throw new RuntimeException("索引文档失败: " + document.getId(), e);
        }
    }

    @Override
    public void bulkIndex(List<SearchDocument> documents) {
        try {
            BulkRequest.Builder bulkBuilder = new BulkRequest.Builder();

            for (SearchDocument doc : documents) {
                SearchDocumentEntity entity = toEntity(doc);
                String indexName = buildIndexName(doc.getIndexType());

                bulkBuilder.operations(op -> op
                        .index(idx -> idx
                                .index(indexName)
                                .id(doc.getId())
                                .document(entity)));
            }

            BulkResponse response = client.bulk(bulkBuilder.build());

            if (response.errors()) {
                log.error("批量索引部分失败");
                response.items().stream()
                        .filter(item -> item.error() != null)
                        .forEach(item -> log.error("文档索引失败: id={}, error={}", item.id(), item.error().reason()));
            } else {
                log.info("批量索引成功: count={}", documents.size());
            }
        } catch (IOException e) {
            throw new RuntimeException("批量索引文档失败", e);
        }
    }

    @Override
    public void delete(String indexType, String id) {
        try {
            String indexName = INDEX_PREFIX + indexType;
            DeleteRequest request = DeleteRequest.of(b -> b
                    .index(indexName)
                    .id(id));

            client.delete(request);
            log.info("文档删除成功: index={}, id={}", indexName, id);
        } catch (IOException e) {
            throw new RuntimeException("删除文档失败: " + id, e);
        }
    }

    @Override
    public void update(SearchDocument document) {
        // ES 的 index 操作本身就是 upsert 语义
        index(document);
    }

    private String buildIndexName(IndexType indexType) {
        return INDEX_PREFIX + indexType.getCode();
    }

    private SearchDocumentEntity toEntity(SearchDocument doc) {
        return SearchDocumentEntity.builder()
                .id(doc.getId())
                .indexType(doc.getIndexType().getCode())
                .tenantId(doc.getTenantId())
                .title(doc.getTitle())
                .body(doc.getBody())
                .attributes(doc.getAttributes())
                .createdAt(doc.getCreatedAt())
                .updatedAt(doc.getUpdatedAt())
                .build();
    }
}