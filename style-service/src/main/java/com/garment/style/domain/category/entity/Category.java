package com.garment.style.domain.category.entity;

import com.garment.common.domain.AggregateRoot;
import com.garment.style.domain.category.event.CategoryCreatedEvent;
import com.garment.style.domain.category.event.CategoryUpdatedEvent;
import lombok.Getter;

@Getter
public class Category extends AggregateRoot {

    private Long id;
    private Long tenantId;
    private Long parentId;
    private String name;
    private Integer sortOrder;
    private String icon;
    /** 0=禁用 1=启用 */
    private Integer status;

    private Category() {}

    public static Category create(Long tenantId, Long parentId, String name,
                                  Integer sortOrder, String icon) {
        Category c = new Category();
        c.tenantId = tenantId;
        c.parentId = parentId != null ? parentId : 0L;
        c.name = name;
        c.sortOrder = sortOrder != null ? sortOrder : 0;
        c.icon = icon;
        c.status = 1;
        c.registerEvent(new CategoryCreatedEvent(tenantId, name));
        return c;
    }

    public void update(String name, Long parentId, Integer sortOrder, String icon) {
        this.name = name;
        this.parentId = parentId;
        this.sortOrder = sortOrder;
        this.icon = icon;
        this.registerEvent(new CategoryUpdatedEvent(this.tenantId, this.id, name));
    }

    public void disable() {
        this.status = 0;
    }

    public void enable() {
        this.status = 1;
    }

    public void setId(Long id) {
        this.id = id;
    }
}