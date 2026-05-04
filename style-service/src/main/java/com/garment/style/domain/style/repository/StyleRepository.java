package com.garment.style.domain.style.repository;

import com.garment.common.interfaces.PageResult;
import com.garment.style.domain.style.entity.Style;
import java.util.List;

public interface StyleRepository {
    void save(Style style);
    void update(Style style);
    Style findById(Long id, Long tenantId);
    Style findByCode(String styleCode, Long tenantId);
    List<Style> findByTenantId(Long tenantId);
    List<Style> findByCategoryId(Long categoryId, Long tenantId);
    void deleteById(Long id, Long tenantId);
    PageResult<Style> pageQuery(Long tenantId, String keyword, Long categoryId,
                                String season, Integer status, int page, int size);
    boolean existsByStyleCode(String styleCode, Long tenantId);
}
