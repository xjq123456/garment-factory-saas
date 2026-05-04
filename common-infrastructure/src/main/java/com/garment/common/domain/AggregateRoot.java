package com.garment.common.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 聚合根基类。
 * <p>
 * DDD 中的聚合根是事务一致性边界，负责维护领域不变量。
 * 子类可通过 {@link #registerEvent(DomainEvent)} 注册领域事件，
 * 由应用层在事务提交后通过 {@link #pullEvents()} 获取并分发。
 * <p>
 * 子类应继承 {@link com.garment.common.infrastructure.BaseEntity} 获取持久化能力，
 * 同时通过组合方式持有本类来管理领域事件。
 */
@Getter
public abstract class AggregateRoot {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    /**
     * 注册一个领域事件，待事务提交后分发。
     */
    public void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    /**
     * 拉取并清空所有已注册的领域事件。
     * 通常在应用服务事务提交后调用。
     */
    public List<DomainEvent> pullEvents() {
        List<DomainEvent> events = Collections.unmodifiableList(new ArrayList<>(domainEvents));
        domainEvents.clear();
        return events;
    }
}