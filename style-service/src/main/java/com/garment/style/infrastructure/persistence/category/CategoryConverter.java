package com.garment.style.infrastructure.persistence.category;

import com.garment.style.domain.category.entity.Category;

public final class CategoryConverter {

    private CategoryConverter() {}

    public static CategoryDO toDO(Category domain) {
        if (domain == null) return null;
        CategoryDO d = new CategoryDO();
        d.setId(domain.getId());
        d.setTenantId(domain.getTenantId());
        d.setParentId(domain.getParentId());
        d.setName(domain.getName());
        d.setSortOrder(domain.getSortOrder());
        d.setIcon(domain.getIcon());
        d.setStatus(domain.getStatus());
        return d;
    }

    public static Category toDomain(CategoryDO d) {
        if (d == null) return null;
        Category c = Category.create(d.getTenantId(), d.getParentId(), d.getName(),
                d.getSortOrder(), d.getIcon());
        c.setId(d.getId());
        try {
            java.lang.reflect.Field statusField = Category.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(c, d.getStatus());
        } catch (Exception e) {
            // ignore
        }
        return c;
    }
}
