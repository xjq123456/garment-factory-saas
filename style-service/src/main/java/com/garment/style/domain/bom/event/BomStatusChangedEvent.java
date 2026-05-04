package com.garment.style.domain.bom.event;

import com.garment.common.domain.DomainEvent;
import lombok.Getter;

@Getter
public class BomStatusChangedEvent extends DomainEvent {
    private final Long tenantId;
    private final Long bomId;
    private final String bomCode;
    private final Integer newStatus;

    public BomStatusChangedEvent(Long tenantId, Long bomId, String bomCode, Integer newStatus) {
        super();
        this.tenantId = tenantId;
        this.bomId = bomId;
        this.bomCode = bomCode;
        this.newStatus = newStatus;
    }
}
