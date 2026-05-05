package com.garment.search.infrastructure.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garment.search.application.index.SearchIndexAppService;
import com.garment.search.domain.index.model.IndexType;
import com.garment.search.domain.index.model.SearchDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 搜索索引事件监听器：消费来自其他微服务的领域事件，维护搜索索引。
 * <p>
 * 监听的 Topic：
 * - style-events：款型相关事件（创建、更新、删除）
 * - order-events：订单相关事件
 * - production-events：生产相关事件
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SearchEventListener {

    private final SearchIndexAppService searchIndexAppService;
    private final ObjectMapper objectMapper;

    /**
     * 监听款型事件，同步索引。
     */
    @KafkaListener(topics = "${search.kafka.topics.style:style-events}", groupId = "${spring.kafka.consumer.group-id:search-service}")
    public void onStyleEvent(ConsumerRecord<String, String> record) {
        log.info("收到款型事件: topic={}, partition={}, key={}", record.topic(), record.partition(), record.key());
        try {
            JsonNode event = objectMapper.readTree(record.value());
            String eventType = event.path("eventType").asText("");
            String tenantId = event.path("tenantId").asText("0");

            JsonNode payload = event.path("payload");
            String styleId = payload.path("styleId").asText("");
            String styleNo = payload.path("styleNo").asText("");
            String styleName = payload.path("styleName").asText("");
            String category = payload.path("category").asText("");
            String season = payload.path("season").asText("");
            String description = payload.path("description").asText("");

            if (eventType.contains("DELETED") || eventType.contains("deleted")) {
                searchIndexAppService.deleteDocument(IndexType.STYLE.getCode(), styleId);
                log.info("删除款型索引: styleId={}", styleId);
            } else {
                Map<String, Object> attrs = new HashMap<>();
                attrs.put("styleNo", styleNo);
                attrs.put("category", category);
                attrs.put("season", season);

                searchIndexAppService.indexDocument(styleId, IndexType.STYLE.getCode(),
                        styleName, styleName + " " + description + " " + styleNo, attrs);
                log.info("同步款型索引: styleId={}, styleNo={}", styleId, styleNo);
            }
        } catch (Exception e) {
            log.error("处理款型事件失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 监听订单事件，同步索引。
     */
    @KafkaListener(topics = "${search.kafka.topics.order:order-events}", groupId = "${spring.kafka.consumer.group-id:search-service}")
    public void onOrderEvent(ConsumerRecord<String, String> record) {
        log.info("收到订单事件: topic={}, key={}", record.topic(), record.key());
        try {
            JsonNode event = objectMapper.readTree(record.value());
            String eventType = event.path("eventType").asText("");
            JsonNode payload = event.path("payload");
            String orderId = payload.path("orderId").asText("");
            String orderNo = payload.path("orderNo").asText("");
            String customerName = payload.path("customerName").asText("");

            if (eventType.contains("DELETED") || eventType.contains("deleted")) {
                searchIndexAppService.deleteDocument(IndexType.ORDER.getCode(), orderId);
            } else {
                Map<String, Object> attrs = new HashMap<>();
                attrs.put("orderNo", orderNo);
                attrs.put("customerName", customerName);
                attrs.put("orderStatus", payload.path("orderStatus").asText(""));

                searchIndexAppService.indexDocument(orderId, IndexType.ORDER.getCode(),
                        orderNo, orderNo + " " + customerName, attrs);
                log.info("同步订单索引: orderId={}, orderNo={}", orderId, orderNo);
            }
        } catch (Exception e) {
            log.error("处理订单事件失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 监听生产事件，同步索引。
     */
    @KafkaListener(topics = "${search.kafka.topics.production:production-events}", groupId = "${spring.kafka.consumer.group-id:search-service}")
    public void onProductionEvent(ConsumerRecord<String, String> record) {
        log.info("收到生产事件: topic={}, key={}", record.topic(), record.key());
        try {
            JsonNode event = objectMapper.readTree(record.value());
            String eventType = event.path("eventType").asText("");
            JsonNode payload = event.path("payload");
            String workOrderId = payload.path("workOrderId").asText("");
            String workOrderNo = payload.path("workOrderNo").asText("");

            if (eventType.contains("DELETED") || eventType.contains("deleted")) {
                searchIndexAppService.deleteDocument(IndexType.PRODUCTION.getCode(), workOrderId);
            } else {
                Map<String, Object> attrs = new HashMap<>();
                attrs.put("workOrderNo", workOrderNo);
                attrs.put("styleNo", payload.path("styleNo").asText(""));
                attrs.put("quantity", payload.path("quantity").asInt(0));

                searchIndexAppService.indexDocument(workOrderId, IndexType.PRODUCTION.getCode(),
                        workOrderNo, workOrderNo + " " + payload.path("styleNo").asText(""), attrs);
                log.info("同步生产索引: workOrderId={}, workOrderNo={}", workOrderId, workOrderNo);
            }
        } catch (Exception e) {
            log.error("处理生产事件失败: {}", e.getMessage(), e);
        }
    }
}