package com.garment.inventory.domain.stocklog.repository;

import com.garment.inventory.domain.stocklog.entity.StockLog;

import java.util.List;

/**
 * 成品库存变动日志仓储接口
 */
public interface StockLogRepository {

    /**
     * 保存日志
     */
    void save(StockLog stockLog);

    /**
     * 批量保存日志
     */
    void saveBatch(List<StockLog> stockLogs);

    /**
     * 根据SKU ID查询日志
     */
    List<StockLog> findBySkuId(Long skuId);

    /**
     * 根据仓库ID和SKU ID查询日志
     */
    List<StockLog> findByWarehouseIdAndSkuId(Long warehouseId, Long skuId);

    /**
     * 根据业务单号查询日志
     */
    List<StockLog> findByBizNo(String bizNo);
}