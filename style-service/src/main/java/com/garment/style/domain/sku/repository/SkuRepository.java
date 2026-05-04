package com.garment.style.domain.sku.repository;

import com.garment.style.domain.sku.entity.Sku;
import java.util.List;

public interface SkuRepository {
    void save(Sku sku);
    void saveBatch(List<Sku> skus);
    void update(Sku sku);
    Sku findById(Long id, Long tenantId);
    Sku findByCode(String skuCode, Long tenantId);
    List<Sku> findByStyleId(Long styleId, Long tenantId);
    List<Sku> findByTenantId(Long tenantId);
    void deleteById(Long id, Long tenantId);
    void deleteByStyleId(Long styleId, Long tenantId);
    boolean existsBySkuCode(String skuCode, Long tenantId);
}
