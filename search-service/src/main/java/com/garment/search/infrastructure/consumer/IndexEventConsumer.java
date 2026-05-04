package com.garment.search.infrastructure.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garment.search.application.index.IndexAppService;
import com.garment.search.domain.index.model.IndexEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Kafka 消费者：消费其他服务发布的领域事件，将其转化为搜索索引操作。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IndexEventConsumer {

    private final IndexAppService indexAppService;
    private final ObjectMapper objectMapper;

    /**
     * 消费款式相关事件（style-service 发布的款式创建/更新/删除事件）。
     */
    @KafkaListener(topics = {"style-events"}, groupId = "${spring.kafka.consumer.group-id:search-service}")
    public void onStyleEvent(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("收到款式事件: topic={}, partition={}, offset={}, key={}",
                record.topic(), record.partition(), record.offset(), record.key());
        try {
            IndexEvent event = objectMapper.readValue(record.value(), IndexEvent.class);
            indexAppService.handleEvent(event);
            ack.acknowledge();
            log.debug("款式事件处理完成: id={}", event.getDocumentId());
        } catch (Exception e) {
            log.error("款式事件处理失败: {}", record.value(), e);
            // 生产环境可考虑发送到死信队列
            ack.acknowledge();
        }
    }

    /**
     * 消费订单相关事件（order-service 发布的订单创建/更新/删除事件）。
     */
    @KafkaListener(topics = {"order-events"}, groupId = "${spring.kafka.consumer.group-id:search-service}")
    public void onOrderEvent(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("收到订单事件: topic={}, partition={}, offset={}, key={}",
                record.topic(), record.partition(), record.offset(), record.key());
        try {
            IndexEvent event = objectMapper.readValue(record.value(), IndexEvent.class);
            indexAppService.handleEvent(event);
            ack.acknowledge();
            log.debug("订单事件处理完成: id={}", event.getDocumentId());
        } catch (Exception e) {
            log.error("订单事件处理失败: {}", record.value(), e);
            ack.acknowledge();
        }
    }

    /**
     * 消费库存相关事件（inventory-service 发布的库存变更事件）。
     */
    @KafkaListener(topics = {"inventory-events"}, groupId = "${spring.kafka.consumer.group-id:search-service}")
    public void onInventoryEvent(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("收到库存事件: topic={}, partition={}, offset={}, key={}",
                record.topic(), record.partition(), record.offset(), record.key());
        try {
            IndexEvent event = objectMapper.readValue(record.value(), IndexEvent.class);
            indexAppService.handleEvent(event);
            ack.acknowledge();
            log.debug("库存事件处理完成: id={}", event.getDocumentId());
        } catch (Exception e) {
            log.error("库存事件处理失败: {}", record.value(), e);
            ack.acknowledge();
        }
    }

    /**
     * 消费生产相关事件（production-service 发布的生产单变更事件）。
     */
    @KafkaListener(topics = {"production-events"}, groupId = "${spring.kafka.consumer.group-id:search-service}")
    public void onProductionEvent(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("收到生产事件: topic={}, partition={}, offset={}, key={}",
                record.topic(), record.partition(), record.offset(), record.key());
        try {
            IndexEvent event = objectMapper.readValue(record.value(), IndexEvent.class);
            indexAppService.handleEvent(event);
            ack.acknowledge();
            log.debug("生产事件处理完成: id={}", event.getDocumentId());
        } catch (Exception e) {
            log.error("生产事件处理失败: {}", record.value(), e);
            ack.acknowledge();
        }
    }

    /**
     * 通用事件消费：处理来自 search-index-events topic 的标准化索引事件。
     */
    @KafkaListener(topics = {"search-index-events"}, groupId = "${spring.kafka.consumer.group-id:search-service}")
    public void onSearchIndexEvent(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("收到搜索索引事件: topic={}, partition={}, offset={}, key={}",
                record.topic(), record.partition(), record.offset(), record.key());
        try {
            IndexEvent event = objectMapper.readValue(record.value(), IndexEvent.class);
            indexAppService.handleEvent(event);
            ack.acknowledge();
            log.debug("搜索索引事件处理完成: id={}", event.getDocumentId());
        } catch (Exception e) {
            log.error("搜索索引事件处理失败: {}", record.value(), e);
            ack.acknowledge();
        }
    }
}