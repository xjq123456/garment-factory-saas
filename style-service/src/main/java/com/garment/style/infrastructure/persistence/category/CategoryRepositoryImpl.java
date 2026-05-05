package com.garment.style.infrastructure.persistence.category;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.garment.style.domain.category.entity.Category;
import com.garment.style.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 分类仓储实现。
 * <p>
 * 多租户隔离由 TenantLineInnerInterceptor 在 SQL 层自动追加 tenant_id 条件。
 */
@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryMapper categoryMapper;

    @Override
    public void save(Category category) {
        CategoryDO categoryDO = CategoryConverter.toDO(category);
        if (categoryDO.getId() == null) {
            categoryMapper.insert(categoryDO);
            category.setId(categoryDO.getId());
        } else {
            categoryMapper.updateById(categoryDO);
        }
    }

    @Override
    public void update(Category category) {
        categoryMapper.updateById(CategoryConverter.toDO(category));
    }

    @Override
    public Category findById(Long id) {
        return CategoryConverter.toDomain(categoryMapper.selectById(id));
    }

    @Override
    public List<Category> findAll() {
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(CategoryDO::getSortOrder);
        return categoryMapper.selectList(wrapper).stream().map(CategoryConverter::toDomain).toList();
    }

    @Override
    public List<Category> findByParentId(Long parentId) {
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryDO::getParentId, parentId)
               .orderByAsc(CategoryDO::getSortOrder);
        return categoryMapper.selectList(wrapper).stream().map(CategoryConverter::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        categoryMapper.deleteById(id);
    }

    @Override
    public boolean existsByName(String name, Long parentId) {
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryDO::getParentId, parentId)
               .eq(CategoryDO::getName, name);
        return categoryMapper.selectCount(wrapper) > 0;
    }
}