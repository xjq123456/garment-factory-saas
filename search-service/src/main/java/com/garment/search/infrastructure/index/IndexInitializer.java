package com.garment.search.infrastructure.index;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.stereotype.Component;

/**
 * ES 索引初始化器：在应用启动时确保必要的索引模板和索引存在。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IndexInitializer {

    private final ElasticsearchOperations elasticsearchOperations;

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
        IndexOperations indexOps = elasticsearchOperations.indexOps(IndexCoordinates.of(indexName));
        return indexOps.exists();
    }

    /**
     * 创建单个索引（含 ik 分词器配置）。
     */
    public void createIndex(String indexName) {
        IndexOperations indexOps = elasticsearchOperations.indexOps(IndexCoordinates.of(indexName));
        if (indexOps.exists()) {
            log.debug("索引已存在: {}", indexName);
            return;
        }

        Document settings = Document.parse("""
                {
                    "index.number_of_shards": 1,
                    "index.number_of_replicas": 0
                }
                """);

        Document mapping = Document.parse("""
                {
                    "properties": {
                        "id": { "type": "keyword" },
                        "indexType": { "type": "keyword" },
                        "tenantId": { "type": "long" },
                        "title": {
                            "type": "text",
                            "analyzer": "ik_max_word",
                            "search_analyzer": "ik_smart",
                            "fields": {
                                "ik": {
                                    "type": "text",
                                    "analyzer": "ik_max_word",
                                    "search_analyzer": "ik_smart"
                                },
                                "keyword": { "type": "keyword", "ignore_above": 256 }
                            }
                        },
                        "body": {
                            "type": "text",
                            "analyzer": "ik_max_word",
                            "search_analyzer": "ik_smart"
                        },
                        "createdAt": { "type": "date", "format": "yyyy-MM-dd'T'HH:mm:ss" },
                        "updatedAt": { "type": "date", "format": "yyyy-MM-dd'T'HH:mm:ss" }
                    }
                }
                """);

        indexOps.create(settings, mapping);
        log.info("创建索引成功: {}", indexName);
    }
}
