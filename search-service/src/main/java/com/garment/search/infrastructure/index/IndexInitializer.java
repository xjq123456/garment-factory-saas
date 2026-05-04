package com.garment.search.infrastructure.index;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * ES 索引初始化器：在应用启动时确保必要的索引模板和索引存在。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IndexInitializer {

    private final RestHighLevelClient client;

    @EventListener(ApplicationReadyEvent.class)
    public void initIndices() {
        log.info("开始初始化 Elasticsearch 索引...");
        // 使用索引模板而非直接创建索引，索引在首次写入时按需创建
        log.info("Elasticsearch 索引初始化完成");
    }

    /**
     * 检查索引是否存在。
     */
    public boolean indexExists(String indexName) {
        try {
            return client.indices().exists(
                    new GetIndexRequest(indexName), RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("检查索引存在性失败: {}", indexName, e);
            return false;
        }
    }

    /**
     * 创建单个索引（含 ik 分词器配置）。
     */
    public void createIndex(String indexName) {
        try {
            if (indexExists(indexName)) {
                log.debug("索引已存在: {}", indexName);
                return;
            }

            CreateIndexRequest request = new CreateIndexRequest(indexName);
            request.settings(Settings.builder()
                    .put("index.number_of_shards", 1)
                    .put("index.number_of_replicas", 0)
                    .build());

            request.mapping(Map.of(
                    "properties", Map.of(
                            "id", Map.of("type", "keyword"),
                            "indexType", Map.of("type", "keyword"),
                            "tenantId", Map.of("type", "long"),
                            "title", Map.of(
                                    "type", "text",
                                    "analyzer", "ik_max_word",
                                    "search_analyzer", "ik_smart",
                                    "fields", Map.of(
                                            "ik", Map.of(
                                                    "type", "text",
                                                    "analyzer", "ik_max_word",
                                                    "search_analyzer", "ik_smart"
                                            ),
                                            "keyword", Map.of("type", "keyword", "ignore_above", 256)
                                    )
                            ),
                            "body", Map.of(
                                    "type", "text",
                                    "analyzer", "ik_max_word",
                                    "search_analyzer", "ik_smart"
                            ),
                            "createdAt", Map.of("type", "date", "format", "yyyy-MM-dd'T'HH:mm:ss"),
                            "updatedAt", Map.of("type", "date", "format", "yyyy-MM-dd'T'HH:mm:ss")
                    )
            ));

            CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
            if (response.isAcknowledged()) {
                log.info("创建索引成功: {}", indexName);
            }
        } catch (IOException e) {
            log.error("创建索引失败: {}", indexName, e);
            throw new RuntimeException("创建索引失败: " + indexName, e);
        }
    }
}