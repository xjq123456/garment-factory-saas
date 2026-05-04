package com.garment.style.domain.category.repository;

import com.garment.style.domain.category.entity.Category;
import java.util.List;

public interface CategoryRepository {
    void save(Category category);
    void update(Category category);
    Category findById(Long id, Long tenantId);
    List<Category> findByTenantId(Long tenantId);
    List<Category> findByParentId(Long parentId, Long tenantId);
    void deleteById(Long id, Long tenantId);
    boolean existsByName(String name, Long parentId, Long tenantId);
}
