package com.garment.inventory.domain.stocklog.repository;

import com.garment.inventory.domain.stocklog.entity.MaterialStockLog;

import java.util.List;

/**
 * 原材料库存变动日志仓储接口
 */
public interface MaterialStockLogRepository {

    /**
     * 保存日志
     */
    void save(MaterialStockLog log);

    /**
     * 批量保存日志
     */
    void saveBatch(List<MaterialStockLog> logs);

    /**
     * 根据原材料ID查询日志
     */
    List<MaterialStockLog> findByMaterialId(Long materialId);

    /**
     * 根据仓库ID和原材料ID查询日志
     */
    List<MaterialStockLog> findByWarehouseIdAndMaterialId(Long warehouseId, Long materialId);

    /**
     * 根据业务单号查询日志
     */
    List<MaterialStockLog> findByBizNo(String bizNo);
}