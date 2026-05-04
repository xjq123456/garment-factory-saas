package com.garment.style.domain.sku.event;

import com.garment.common.domain.DomainEvent;
import lombok.Getter;

@Getter
public class SkuUpdatedEvent extends DomainEvent {
    private final Long tenantId;
    private final Long skuId;
    private final String skuCode;

    public SkuUpdatedEvent(Long tenantId, Long skuId, String skuCode) {
        super();
        this.tenantId = tenantId;
        this.skuId = skuId;
        this.skuCode = skuCode;
    }
}