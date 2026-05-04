package com.garment.style.domain.bom.repository;

import com.garment.style.domain.bom.entity.Bom;
import java.util.List;

public interface BomRepository {
    void save(Bom bom);
    void update(Bom bom);
    Bom findById(Long id, Long tenantId);
    Bom findByCode(String bomCode, Long tenantId);
    List<Bom> findByStyleId(Long styleId, Long tenantId);
    List<Bom> findByTenantId(Long tenantId);
    void deleteById(Long id, Long tenantId);
    void deleteByStyleId(Long styleId, Long tenantId);
    boolean existsByBomCode(String bomCode, Long tenantId);
}
