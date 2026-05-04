package com.garment.style.domain.style.event;

import com.garment.common.domain.DomainEvent;
import lombok.Getter;

@Getter
public class StylePublishedEvent extends DomainEvent {
    private final Long tenantId;
    private final Long styleId;
    private final String styleCode;

    public StylePublishedEvent(Long tenantId, Long styleId, String styleCode) {
        super();
        this.tenantId = tenantId;
        this.styleId = styleId;
        this.styleCode = styleCode;
    }
}
