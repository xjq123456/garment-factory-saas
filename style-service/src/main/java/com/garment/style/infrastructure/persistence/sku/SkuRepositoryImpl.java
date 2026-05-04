package com.garment.style.infrastructure.persistence.sku;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.garment.style.domain.sku.entity.Sku;
import com.garment.style.domain.sku.repository.SkuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public void deleteById(Long id, Long tenantId) {
        LambdaQueryWrapper<SkuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuDO::getId, id).eq(SkuDO::getTenantId, tenantId);
        skuMapper.delete(wrapper);
    }

    @Override
    public void deleteByStyleId(Long styleId, Long tenantId) {
        LambdaQueryWrapper<SkuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuDO::getStyleId, styleId).eq(SkuDO::getTenantId, tenantId);
        skuMapper.delete(wrapper);
    }

    @Override
    public Sku findById(Long id, Long tenantId) {
        LambdaQueryWrapper<SkuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuDO::getId, id).eq(SkuDO::getTenantId, tenantId);
        return SkuConverter.toDomain(skuMapper.selectOne(wrapper));
    }

    @Override
    public Sku findByCode(String skuCode, Long tenantId) {
        LambdaQueryWrapper<SkuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuDO::getSkuCode, skuCode).eq(SkuDO::getTenantId, tenantId);
        return SkuConverter.toDomain(skuMapper.selectOne(wrapper));
    }

    @Override
    public List<Sku> findByStyleId(Long styleId, Long tenantId) {
        LambdaQueryWrapper<SkuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuDO::getStyleId, styleId).eq(SkuDO::getTenantId, tenantId);
        wrapper.orderByAsc(SkuDO::getSortOrder);
        return skuMapper.selectList(wrapper).stream().map(SkuConverter::toDomain).toList();
    }

    @Override
    public List<Sku> findByTenantId(Long tenantId) {
        LambdaQueryWrapper<SkuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuDO::getTenantId, tenantId).orderByDesc(SkuDO::getCreatedAt);
        return skuMapper.selectList(wrapper).stream().map(SkuConverter::toDomain).toList();
    }

    @Override
    public boolean existsBySkuCode(String skuCode, Long tenantId) {
        LambdaQueryWrapper<SkuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuDO::getSkuCode, skuCode).eq(SkuDO::getTenantId, tenantId);
        return skuMapper.selectCount(wrapper) > 0;
    }
}
