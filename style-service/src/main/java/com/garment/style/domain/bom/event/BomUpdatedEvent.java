package com.garment.style.domain.bom.event;

import com.garment.common.domain.DomainEvent;
import lombok.Getter;

@Getter
public class BomUpdatedEvent extends DomainEvent {
    private final Long tenantId;
    private final Long bomId;
    private final String bomCode;

    public BomUpdatedEvent(Long tenantId, Long bomId, String bomCode) {
        super();
        this.tenantId = tenantId;
        this.bomId = bomId;
        this.bomCode = bomCode;
    }
}
