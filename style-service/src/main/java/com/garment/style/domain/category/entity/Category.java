package com.garment.style.domain.category.entity;

import com.garment.common.domain.AggregateRoot;
import com.garment.common.domain.DomainEvent;
import com.garment.style.domain.category.event.CategoryCreatedEvent;
import com.garment.style.domain.category.event.CategoryUpdatedEvent;
import lombok.Getter;

@Getter
public class Category extends AggregateRoot {

    private Long parentId;
    private String name;
    private Integer sortOrder;
    private String icon;
    /** 0=禁用 1=启用 */
    private Integer status;

    private Category() {}

    /**
     * 创建分类（工厂方法）。
     * <p>
     * 注意：此方法不产生领域事件，由应用层负责创建并发布 {@link CategoryCreatedEvent}。
     */
    public static Category create(Long tenantId, Long parentId, String name,
                                  Integer sortOrder, String icon) {
        Category c = new Category();
        c.tenantId = tenantId;
        c.parentId = parentId != null ? parentId : 0L;
        c.name = name;
        c.sortOrder = sortOrder != null ? sortOrder : 0;
        c.icon = icon;
        c.status = 1;
        return c;
    }

    public DomainEvent update(String name, Long parentId, Integer sortOrder, String icon) {
        this.name = name;
        this.parentId = parentId;
        this.sortOrder = sortOrder;
        this.icon = icon;
        return new CategoryUpdatedEvent(this.tenantId, this.id, name);
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

    /** 仅供基础设施层从数据库还原时使用，业务代码不应调用 */
    public void overrideStatus(Integer status) {
        this.status = status;
    }
}
