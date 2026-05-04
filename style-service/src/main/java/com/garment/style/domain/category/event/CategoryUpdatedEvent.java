package com.garment.style.domain.category.event;

import com.garment.common.domain.DomainEvent;
import lombok.Getter;

@Getter
public class CategoryUpdatedEvent extends DomainEvent {
    private final Long tenantId;
    private final Long categoryId;
    private final String categoryName;

    public CategoryUpdatedEvent(Long tenantId, Long categoryId, String categoryName) {
        super();
        this.tenantId = tenantId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}