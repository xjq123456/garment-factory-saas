package com.garment.style.domain.category.repository;

import com.garment.style.domain.category.entity.Category;
import java.util.List;

/**
 * 分类仓储接口。
 * <p>
 * 多租户隔离由基础设施层自动处理，业务代码无需显式传递 tenantId。
 */
public interface CategoryRepository {
    void save(Category category);
    void update(Category category);
    Category findById(Long id);
    List<Category> findAll();
    List<Category> findByParentId(Long parentId);
    void deleteById(Long id);
    boolean existsByName(String name, Long parentId);
}