package com.garment.style.domain.style.repository;

import com.garment.common.interfaces.PageResult;
import com.garment.style.domain.style.entity.Style;
import java.util.List;

/**
 * 款式仓储接口。
 * <p>
 * 多租户隔离由基础设施层自动处理，业务代码无需显式传递 tenantId。
 */
public interface StyleRepository {
    void save(Style style);
    void update(Style style);
    Style findById(Long id);
    Style findByCode(String styleCode);
    List<Style> findAll();
    List<Style> findByCategoryId(Long categoryId);
    void deleteById(Long id);
    PageResult<Style> pageQuery(String keyword, Long categoryId,
                                String season, Integer status, int page, int size);
    boolean existsByStyleCode(String styleCode);
}