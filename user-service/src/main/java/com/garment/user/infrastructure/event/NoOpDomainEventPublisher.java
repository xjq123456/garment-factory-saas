package com.garment.user.infrastructure.event;

import com.garment.common.domain.DomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * 无 Kafka 时的降级事件发布器（仅打印日志）。
 */
@Slf4j
@Component
@ConditionalOnMissingBean(DomainEventPublisher.class)
public class NoOpDomainEventPublisher implements DomainEventPublisher {

    @Override
    public void publish(DomainEvent event) {
        log.info("[NoOp] 领域事件: source={}, eventId={}", event.getSource(), event.getEventId());
    }
}
