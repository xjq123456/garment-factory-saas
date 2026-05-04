package com.garment.style.domain.style.event;

import com.garment.common.domain.DomainEvent;
import lombok.Getter;

@Getter
public class StyleCreatedEvent extends DomainEvent {
    private final Long tenantId;
    private final String styleCode;
    private final String styleName;

    public StyleCreatedEvent(Long tenantId, String styleCode, String styleName) {
        super();
        this.tenantId = tenantId;
        this.styleCode = styleCode;
        this.styleName = styleName;
    }
}