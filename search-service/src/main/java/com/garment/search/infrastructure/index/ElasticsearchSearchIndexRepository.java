package com.garment.search.infrastructure.index;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garment.search.domain.index.model.IndexType;
import com.garment.search.domain.index.model.SearchDocument;
import com.garment.search.domain.index.repository.SearchIndexRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
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

    private final RestHighLevelClient client;
    private final ObjectMapper objectMapper;

    private static final String INDEX_PREFIX = "garment_";

    @Override
    public void index(SearchDocument document) {
        try {
            Map<String, Object> source = buildSource(document);
            String indexName = resolveIndexName(document.getIndexType(), document.getTenantId());

            IndexRequest request = new IndexRequest(indexName)
                    .id(document.getId())
                    .source(source, XContentType.JSON);

            client.index(request, RequestOptions.DEFAULT);
            log.debug("索引文档成功: index={}, id={}", indexName, document.getId());
        } catch (Exception e) {
            log.error("索引文档失败: type={}, id={}", document.getIndexType(), document.getId(), e);
            throw new RuntimeException("索引文档失败", e);
        }
    }

    @Override
    public void bulkIndex(List<SearchDocument> documents) {
        try {
            BulkRequest bulkRequest = new BulkRequest();

            for (SearchDocument document : documents) {
                Map<String, Object> source = buildSource(document);
                String indexName = resolveIndexName(document.getIndexType(), document.getTenantId());

                IndexRequest request = new IndexRequest(indexName)
                        .id(document.getId())
                        .source(source, XContentType.JSON);
                bulkRequest.add(request);
            }

            BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (response.hasFailures()) {
                log.warn("批量索引部分失败: {}", response.buildFailureMessage());
            } else {
                log.debug("批量索引成功: count={}", documents.size());
            }
        } catch (Exception e) {
            log.error("批量索引失败: count={}", documents.size(), e);
            throw new RuntimeException("批量索引失败", e);
        }
    }

    @Override
    public void delete(String indexType, String id) {
        try {
            // 删除时需要遍历所有租户的索引，这里使用通配符
            // 实际场景中 tenantId 应该已知，此处简化处理
            DeleteRequest request = new DeleteRequest(INDEX_PREFIX + indexType + "*")
                    .id(id);
            client.delete(request, RequestOptions.DEFAULT);
            log.debug("删除文档成功: indexType={}, id={}", indexType, id);
        } catch (Exception e) {
            log.error("删除文档失败: indexType={}, id={}", indexType, id, e);
            throw new RuntimeException("删除文档失败", e);
        }
    }

    @Override
    public void update(SearchDocument document) {
        try {
            Map<String, Object> source = buildSource(document);
            String indexName = resolveIndexName(document.getIndexType(), document.getTenantId());

            UpdateRequest request = new UpdateRequest(indexName, document.getId())
                    .doc(source, XContentType.JSON)
                    .docAsUpsert(true);

            client.update(request, RequestOptions.DEFAULT);
            log.debug("更新文档成功: index={}, id={}", indexName, document.getId());
        } catch (Exception e) {
            log.error("更新文档失败: type={}, id={}", document.getIndexType(), document.getId(), e);
            throw new RuntimeException("更新文档失败", e);
        }
    }

    /**
     * 构建 ES 文档源数据。
     */
    private Map<String, Object> buildSource(SearchDocument document) {
        Map<String, Object> source = new HashMap<>();
        source.put("id", document.getId());
        source.put("indexType", document.getIndexType().name());
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

    /**
     * 根据索引类型和租户ID生成索引名。
     * 格式: garment_{indexType}_{tenantId}（实现多租户数据隔离）
     */
    private String resolveIndexName(IndexType indexType, Long tenantId) {
        return INDEX_PREFIX + indexType.name().toLowerCase() + "_" + tenantId;
    }
}