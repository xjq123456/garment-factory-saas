package com.garment.style.application.category;

import com.garment.common.domain.AuthUserContext;
import com.garment.common.domain.BizException;
import com.garment.common.domain.DomainEvent;
import com.garment.style.application.category.dto.CreateCategoryCommand;
import com.garment.style.application.category.dto.UpdateCategoryCommand;
import com.garment.style.domain.category.entity.Category;
import com.garment.style.domain.category.event.CategoryCreatedEvent;
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
        Long tenantId = AuthUserContext.requireTenantId();
        if (categoryRepository.existsByName(cmd.getName(), cmd.getParentId())) {
            throw new BizException("同级分类名称已存在: " + cmd.getName());
        }
        Category category = Category.create(tenantId, cmd.getParentId(),
                cmd.getName(), cmd.getSortOrder(), cmd.getIcon());
        categoryRepository.save(category);
        eventPublisher.publish(new CategoryCreatedEvent(tenantId, cmd.getName()));
        return category;
    }

    @Transactional(rollbackFor = Exception.class)
    public Category updateCategory(Long categoryId, UpdateCategoryCommand cmd) {
        Category category = categoryRepository.findById(categoryId);
        if (category == null) {
            throw new BizException("分类不存在: " + categoryId);
        }
        DomainEvent event = category.update(cmd.getName(), cmd.getParentId(), cmd.getSortOrder(), cmd.getIcon());
        categoryRepository.update(category);
        eventPublisher.publish(event);
        return category;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId);
        if (category == null) {
            throw new BizException("分类不存在: " + categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }

    @Transactional(readOnly = true)
    public Category getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId);
        if (category == null) {
            throw new BizException("分类不存在: " + categoryId);
        }
        return category;
    }

    @Transactional(readOnly = true)
    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Category> listByParentId(Long parentId) {
        return categoryRepository.findByParentId(parentId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void enableCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId);
        if (category == null) throw new BizException("分类不存在: " + categoryId);
        category.enable();
        categoryRepository.update(category);
    }

    @Transactional(rollbackFor = Exception.class)
    public void disableCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId);
        if (category == null) throw new BizException("分类不存在: " + categoryId);
        category.disable();
        categoryRepository.update(category);
    }
}
