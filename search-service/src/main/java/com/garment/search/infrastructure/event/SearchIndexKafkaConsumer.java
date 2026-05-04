package com.garment.search.infrastructure.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garment.search.domain.index.model.IndexType;
import com.garment.search.domain.index.model.SearchDocument;
import com.garment.search.domain.index.repository.SearchIndexRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Kafka 事件消费者：监听各业务服务发布的数据变更事件，
 * 将数据同步到 Elasticsearch 索引。
 * <p>
 * 监听 topic 规则: {@code garment.search.index.*}（通配多个业务域事件）。
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.kafka.bootstrap-servers")
public class SearchIndexKafkaConsumer {

    private final SearchIndexRepository searchIndexRepository;
    private final ObjectMapper objectMapper;

    /**
     * 监听各业务服务发布的领域事件。
     * topic 格式: garment.{service}.{event_name}
     * 这里统一订阅搜索相关的索引同步事件。
     */
    @KafkaListener(
            topics = {
                    "garment.style.style_created",
                    "garment.style.style_updated",
                    "garment.style.style_deleted",
                    "garment.style.sku_created",
                    "garment.style.sku_updated",
                    "garment.style.sku_deleted"
            },
            groupId = "search-service-group"
    )
    public void onStyleEvent(ConsumerRecord<String, String> record) {
        try {
            log.info("收到领域事件: topic={}, key={}, partition={}, offset={}",
                    record.topic(), record.key(), record.partition(), record.offset());

            SearchIndexEvent event = objectMapper.readValue(record.value(), SearchIndexEvent.class);
            processEvent(event);

        } catch (Exception e) {
            log.error("处理领域事件失败: topic={}, key={}", record.topic(), record.key(), e);
        }
    }

    /**
     * 处理搜索索引事件。
     */
    private void processEvent(SearchIndexEvent event) {
        if (event == null) {
            log.warn("收到空的索引事件，忽略");
            return;
        }

        String operation = event.getOperation();
        if (operation == null) {
            log.warn("事件操作类型为空，忽略");
            return;
        }

        switch (operation.toUpperCase()) {
            case "INDEX", "CREATE" -> handleIndex(event);
            case "UPDATE" -> handleUpdate(event);
            case "DELETE" -> handleDelete(event);
            default -> log.warn("未知的事件操作类型: {}", operation);
        }
    }

    private void handleIndex(SearchIndexEvent event) {
        SearchDocument document = toSearchDocument(event);
        searchIndexRepository.index(document);
        log.info("文档索引成功: indexType={}, id={}", event.getIndexType(), event.getDocumentId());
    }

    private void handleUpdate(SearchIndexEvent event) {
        SearchDocument document = toSearchDocument(event);
        searchIndexRepository.update(document);
        log.info("文档更新成功: indexType={}, id={}", event.getIndexType(), event.getDocumentId());
    }

    private void handleDelete(SearchIndexEvent event) {
        searchIndexRepository.delete(event.getIndexType(), event.getDocumentId());
        log.info("文档删除成功: indexType={}, id={}", event.getIndexType(), event.getDocumentId());
    }

    private SearchDocument toSearchDocument(SearchIndexEvent event) {
        return SearchDocument.builder()
                .id(event.getDocumentId())
                .indexType(IndexType.fromCode(event.getIndexType()))
                .tenantId(event.getTenantId())
                .title(event.getTitle())
                .body(event.getBody())
                .attributes(event.getAttributes())
                .createdAt(event.getTimestamp())
                .updatedAt(event.getTimestamp())
                .build();
    }
}