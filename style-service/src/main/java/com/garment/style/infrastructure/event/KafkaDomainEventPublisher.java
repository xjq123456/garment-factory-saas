package com.garment.style.infrastructure.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garment.common.domain.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * 基于 Kafka 的领域事件发布器。
 * <p>
 * 仅在 {@code spring.kafka.bootstrap-servers} 配置存在时生效。
 * topic 规则: {@code garment.style.<事件类名小写蛇形>}。
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.kafka.bootstrap-servers")
public class KafkaDomainEventPublisher implements DomainEventPublisher {

    private static final String TOPIC_PREFIX = "garment.style.";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(DomainEvent event) {
        String topic = TOPIC_PREFIX + toSnakeCase(event.getClass().getSimpleName());
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, event.getEventId(), payload)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("领域事件发送失败: topic={}, eventId={}", topic, event.getEventId(), ex);
                        } else {
                            log.debug("领域事件已发送: topic={}, eventId={}, partition={}",
                                    topic, event.getEventId(),
                                    result.getRecordMetadata().partition());
                        }
                    });
        } catch (JsonProcessingException e) {
            log.error("领域事件序列化失败: {}", event.getClass().getSimpleName(), e);
        }
    }

    private static String toSnakeCase(String camelCase) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < camelCase.length(); i++) {
            char c = camelCase.charAt(i);
            if (i > 0 && Character.isUpperCase(c)) {
                sb.append('_');
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }
}
