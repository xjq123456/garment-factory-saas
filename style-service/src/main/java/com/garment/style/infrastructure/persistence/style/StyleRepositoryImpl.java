package com.garment.style.infrastructure.persistence.style;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garment.common.interfaces.PageResult;
import com.garment.style.domain.style.entity.Style;
import com.garment.style.domain.style.repository.StyleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 款式仓储实现。
 * <p>
 * 多租户隔离由 TenantLineInnerInterceptor 在 SQL 层自动追加 tenant_id 条件。
 */
@Repository
@RequiredArgsConstructor
public class StyleRepositoryImpl implements StyleRepository {

    private final StyleMapper styleMapper;

    @Override
    public void save(Style style) {
        StyleDO styleDO = StyleConverter.toDO(style);
        if (styleDO.getId() == null) {
            styleMapper.insert(styleDO);
            style.setId(styleDO.getId());
        } else {
            styleMapper.updateById(styleDO);
        }
    }

    @Override
    public void update(Style style) {
        styleMapper.updateById(StyleConverter.toDO(style));
    }

    @Override
    public void deleteById(Long id) {
        styleMapper.deleteById(id);
    }

    @Override
    public Style findById(Long id) {
        return StyleConverter.toDomain(styleMapper.selectById(id));
    }

    @Override
    public Style findByCode(String styleCode) {
        LambdaQueryWrapper<StyleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StyleDO::getStyleCode, styleCode);
        return StyleConverter.toDomain(styleMapper.selectOne(wrapper));
    }

    @Override
    public List<Style> findAll() {
        LambdaQueryWrapper<StyleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(StyleDO::getCreatedAt);
        return styleMapper.selectList(wrapper).stream().map(StyleConverter::toDomain).toList();
    }

    @Override
    public List<Style> findByCategoryId(Long categoryId) {
        LambdaQueryWrapper<StyleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StyleDO::getCategoryId, categoryId)
               .orderByDesc(StyleDO::getCreatedAt);
        return styleMapper.selectList(wrapper).stream().map(StyleConverter::toDomain).toList();
    }

    @Override
    public PageResult<Style> pageQuery(String keyword, Long categoryId,
                                        String season, Integer status, int page, int size) {
        LambdaQueryWrapper<StyleDO> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(StyleDO::getStyleName, keyword)
                              .or().like(StyleDO::getStyleCode, keyword));
        }
        if (categoryId != null) wrapper.eq(StyleDO::getCategoryId, categoryId);
        if (season != null && !season.isEmpty()) wrapper.eq(StyleDO::getSeason, season);
        if (status != null) wrapper.eq(StyleDO::getStatus, status);
        wrapper.orderByDesc(StyleDO::getCreatedAt);
        Page<StyleDO> result = styleMapper.selectPage(new Page<>(page, size), wrapper);
        List<Style> domainList = result.getRecords().stream()
                .map(StyleConverter::toDomain).toList();
        return new PageResult<>(result.getCurrent(), result.getSize(),
                result.getTotal(), result.getPages(), domainList);
    }

    @Override
    public boolean existsByStyleCode(String styleCode) {
        LambdaQueryWrapper<StyleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StyleDO::getStyleCode, styleCode);
        return styleMapper.selectCount(wrapper) > 0;
    }
}