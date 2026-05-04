package com.garment.style.domain.style.event;

import com.garment.common.domain.DomainEvent;
import lombok.Getter;

@Getter
public class StyleStatusChangedEvent extends DomainEvent {
    private final Long tenantId;
    private final Long styleId;
    private final String styleCode;
    /** 0=草稿 1=已发布 2=已停用 */
    private final Integer newStatus;

    public StyleStatusChangedEvent(Long tenantId, Long styleId, String styleCode, Integer newStatus) {
        super();
        this.tenantId = tenantId;
        this.styleId = styleId;
        this.styleCode = styleCode;
        this.newStatus = newStatus;
    }
}