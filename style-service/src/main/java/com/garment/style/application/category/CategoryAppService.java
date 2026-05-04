package com.garment.style.application.category;

import com.garment.common.domain.BizException;
import com.garment.common.domain.DomainEvent;
import com.garment.common.domain.TenantContext;
import com.garment.style.application.category.dto.CreateCategoryCommand;
import com.garment.style.application.category.dto.UpdateCategoryCommand;
import com.garment.style.domain.category.entity.Category;
import com.garment.style.domain.category.repository.CategoryRepository;
import com.garment.style.infrastructure.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryAppService {

    private final CategoryRepository categoryRepository;
    private final DomainEventPublisher eventPublisher;

    @Transactional(rollbackFor = Exception.class)
    public Category createCategory(CreateCategoryCommand cmd) {
        Long tenantId = TenantContext.requireTenantId();
        if (categoryRepository.existsByName(cmd.getName(), cmd.getParentId(), tenantId)) {
            throw new BizException("同级分类名称已存在: " + cmd.getName());
        }
        Category category = Category.create(tenantId, cmd.getParentId(),
                cmd.getName(), cmd.getSortOrder(), cmd.getIcon());
        categoryRepository.save(category);
        publishEvents(category);
        return category;
    }

    @Transactional(rollbackFor = Exception.class)
    public Category updateCategory(Long categoryId, UpdateCategoryCommand cmd) {
        Long tenantId = TenantContext.requireTenantId();
        Category category = categoryRepository.findById(categoryId, tenantId);
        if (category == null) {
            throw new BizException("分类不存在: " + categoryId);
        }
        category.update(cmd.getName(), cmd.getParentId(), cmd.getSortOrder(), cmd.getIcon());
        categoryRepository.update(category);
        publishEvents(category);
        return category;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long categoryId) {
        Long tenantId = TenantContext.requireTenantId();
        Category category = categoryRepository.findById(categoryId, tenantId);
        if (category == null) {
            throw new BizException("分类不存在: " + categoryId);
        }
        categoryRepository.deleteById(categoryId, tenantId);
    }

    @Transactional(readOnly = true)
    public Category getCategory(Long categoryId) {
        Long tenantId = TenantContext.requireTenantId();
        Category category = categoryRepository.findById(categoryId, tenantId);
        if (category == null) {
            throw new BizException("分类不存在: " + categoryId);
        }
        return category;
    }

    @Transactional(readOnly = true)
    public List<Category> listCategories() {
        return categoryRepository.findByTenantId(TenantContext.requireTenantId());
    }

    @Transactional(readOnly = true)
    public List<Category> listByParentId(Long parentId) {
        return categoryRepository.findByParentId(parentId, TenantContext.requireTenantId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void enableCategory(Long categoryId) {
        Long tenantId = TenantContext.requireTenantId();
        Category category = categoryRepository.findById(categoryId, tenantId);
        if (category == null) throw new BizException("分类不存在: " + categoryId);
        category.enable();
        categoryRepository.update(category);
    }

    @Transactional(rollbackFor = Exception.class)
    public void disableCategory(Long categoryId) {
        Long tenantId = TenantContext.requireTenantId();
        Category category = categoryRepository.findById(categoryId, tenantId);
        if (category == null) throw new BizException("分类不存在: " + categoryId);
        category.disable();
        categoryRepository.update(category);
    }

    private void publishEvents(Category category) {
        List<DomainEvent> events = category.pullEvents();
        events.forEach(eventPublisher::publish);
    }
}
