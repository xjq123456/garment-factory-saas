package com.garment.style.domain.category.event;

import com.garment.common.domain.DomainEvent;
import lombok.Getter;

@Getter
public class CategoryCreatedEvent extends DomainEvent {
    private final Long tenantId;
    private final String categoryName;

    public CategoryCreatedEvent(Long tenantId, String categoryName) {
        super();
        this.tenantId = tenantId;
        this.categoryName = categoryName;
    }
}