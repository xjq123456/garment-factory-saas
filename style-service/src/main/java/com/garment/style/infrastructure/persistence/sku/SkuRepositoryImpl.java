package com.garment.style.infrastructure.persistence.sku;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.garment.style.domain.sku.entity.Sku;
import com.garment.style.domain.sku.repository.SkuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SKU 仓储实现。
 * <p>
 * 多租户隔离由 {@code MybatisPlusTenantConfig} 的 TenantLineInnerInterceptor
 * 在 SQL 层自动追加 tenant_id 条件，此处无需手动过滤。
 */
@Repository
@RequiredArgsConstructor
public class SkuRepositoryImpl implements SkuRepository {

    private final SkuMapper skuMapper;

    @Override
    public void save(Sku sku) {
        SkuDO skuDO = SkuConverter.toDO(sku);
        if (skuDO.getId() == null) {
            skuMapper.insert(skuDO);
            sku.setId(skuDO.getId());
        } else {
            skuMapper.updateById(skuDO);
        }
    }

    @Override
    public void saveBatch(List<Sku> skus) {
        for (Sku sku : skus) {
            save(sku);
        }
    }

    @Override
    public void update(Sku sku) {
        skuMapper.updateById(SkuConverter.toDO(sku));
    }

    @Override
    public void deleteById(Long id) {
        skuMapper.deleteById(id);
    }

    @Override
    public void deleteByStyleId(Long styleId) {
        LambdaQueryWrapper<SkuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuDO::getStyleId, styleId);
        skuMapper.delete(wrapper);
    }

    @Override
    public Sku findById(Long id) {
        return SkuConverter.toDomain(skuMapper.selectById(id));
    }

    @Override
    public Sku findByCode(String skuCode) {
        LambdaQueryWrapper<SkuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuDO::getSkuCode, skuCode);
        return SkuConverter.toDomain(skuMapper.selectOne(wrapper));
    }

    @Override
    public List<Sku> findByStyleId(Long styleId) {
        LambdaQueryWrapper<SkuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuDO::getStyleId, styleId).orderByAsc(SkuDO::getSortOrder);
        return skuMapper.selectList(wrapper).stream().map(SkuConverter::toDomain).toList();
    }

    @Override
    public List<Sku> findAll() {
        LambdaQueryWrapper<SkuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SkuDO::getCreatedAt);
        return skuMapper.selectList(wrapper).stream().map(SkuConverter::toDomain).toList();
    }

    @Override
    public boolean existsBySkuCode(String skuCode) {
        LambdaQueryWrapper<SkuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuDO::getSkuCode, skuCode);
        return skuMapper.selectCount(wrapper) > 0;
    }
}