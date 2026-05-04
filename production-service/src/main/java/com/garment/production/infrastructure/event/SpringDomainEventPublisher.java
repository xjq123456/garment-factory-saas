package com.garment.production.infrastructure.event;

import com.garment.common.domain.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 基于Spring事件机制的领域事件发布器
 *
 * 后续可替换为Kafka实现，实现跨服务事件通信
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SpringDomainEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publish(DomainEvent event) {
        log.info("发布领域事件: eventType={}, eventId={}", event.getEventType(), event.getEventId());
        eventPublisher.publishEvent(event);
    }
}