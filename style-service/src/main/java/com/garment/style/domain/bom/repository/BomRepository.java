package com.garment.style.domain.bom.repository;

import com.garment.style.domain.bom.entity.Bom;
import java.util.List;

/**
 * BOM 仓储接口。
 * <p>
 * 多租户隔离由基础设施层自动处理，业务代码无需显式传递 tenantId。
 */
public interface BomRepository {
    void save(Bom bom);
    void update(Bom bom);
    Bom findById(Long id);
    Bom findByCode(String bomCode);
    List<Bom> findByStyleId(Long styleId);
    List<Bom> findAll();
    void deleteById(Long id);
    void deleteByStyleId(Long styleId);
    boolean existsByBomCode(String bomCode);
}