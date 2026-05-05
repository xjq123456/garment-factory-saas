package com.garment.style.domain.sku.repository;

import com.garment.style.domain.sku.entity.Sku;
import java.util.List;

/**
 * SKU 仓储接口。
 * <p>
 * 多租户隔离由基础设施层（MyBatis-Plus TenantLineInnerInterceptor + AuthUserContext）自动处理，
 * 业务代码无需在接口中显式传递 tenantId。
 */
public interface SkuRepository {
    void save(Sku sku);
    void saveBatch(List<Sku> skus);
    void update(Sku sku);
    Sku findById(Long id);
    Sku findByCode(String skuCode);
    List<Sku> findByStyleId(Long styleId);
    List<Sku> findAll();
    void deleteById(Long id);
    void deleteByStyleId(Long styleId);
    boolean existsBySkuCode(String skuCode);
}