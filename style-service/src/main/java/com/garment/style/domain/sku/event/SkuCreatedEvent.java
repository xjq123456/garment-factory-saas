package com.garment.style.domain.sku.event;

import com.garment.common.domain.DomainEvent;
import lombok.Getter;

@Getter
public class SkuCreatedEvent extends DomainEvent {
    private final Long tenantId;
    private final Long styleId;
    private final String skuCode;

    public SkuCreatedEvent(Long tenantId, Long styleId, String skuCode) {
        super();
        this.tenantId = tenantId;
        this.styleId = styleId;
        this.skuCode = skuCode;
    }
}