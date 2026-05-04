package com.garment.style.infrastructure.event;

import com.garment.common.domain.DomainEvent;

/**
 * 领域事件发布器接口。
 */
public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
