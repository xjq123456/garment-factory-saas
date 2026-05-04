package com.garment.common.domain;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 领域事件基类。
 * <p>
 * 聚合根在状态变更时发布领域事件，由应用层订阅处理。
 * 每个事件携带唯一事件 ID、发生时间和事件来源（聚合根类名）。
 */
@Getter
public abstract class DomainEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 事件唯一标识 */
    private final String eventId;

    /** 事件发生时间 */
    private final LocalDateTime occurredAt;

    /** 事件来源（聚合根类名） */
    private final String source;

    protected DomainEvent() {
        this.eventId = UUID.randomUUID().toString().replace("-", "");
        this.occurredAt = LocalDateTime.now();
        this.source = this.getClass().getSimpleName();
    }
}