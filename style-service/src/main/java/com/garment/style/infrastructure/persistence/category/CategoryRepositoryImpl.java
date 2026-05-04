package com.garment.style.infrastructure.persistence.category;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.garment.style.domain.category.entity.Category;
import com.garment.style.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public Category findById(Long id, Long tenantId) {
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryDO::getId, id).eq(CategoryDO::getTenantId, tenantId);
        return CategoryConverter.toDomain(categoryMapper.selectOne(wrapper));
    }

    @Override
    public List<Category> findByTenantId(Long tenantId) {
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryDO::getTenantId, tenantId).orderByAsc(CategoryDO::getSortOrder);
        return categoryMapper.selectList(wrapper).stream().map(CategoryConverter::toDomain).toList();
    }

    @Override
    public List<Category> findByParentId(Long parentId, Long tenantId) {
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryDO::getTenantId, tenantId)
               .eq(CategoryDO::getParentId, parentId)
               .orderByAsc(CategoryDO::getSortOrder);
        return categoryMapper.selectList(wrapper).stream().map(CategoryConverter::toDomain).toList();
    }

    @Override
    public void deleteById(Long id, Long tenantId) {
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryDO::getId, id).eq(CategoryDO::getTenantId, tenantId);
        categoryMapper.delete(wrapper);
    }

    @Override
    public boolean existsByName(String name, Long parentId, Long tenantId) {
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryDO::getTenantId, tenantId)
               .eq(CategoryDO::getName, name)
               .eq(CategoryDO::getParentId, parentId);
        return categoryMapper.selectCount(wrapper) > 0;
    }
}
