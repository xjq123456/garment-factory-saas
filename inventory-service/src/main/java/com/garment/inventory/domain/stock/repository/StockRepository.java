package com.garment.inventory.domain.stock.repository;

import com.garment.inventory.domain.stock.entity.Stock;

import java.util.List;
import java.util.Optional;

/**
 * 成品库存仓储接口
 */
public interface StockRepository {

    /**
     * 保存库存
     */
    void save(Stock stock);

    /**
     * 更新库存
     */
    void update(Stock stock);

    /**
     * 根据ID查找库存
     */
    Optional<Stock> findById(Long id);

    /**
     * 根据仓库ID和SKU ID查找库存
     */
    Optional<Stock> findByWarehouseIdAndSkuId(Long warehouseId, Long skuId);

    /**
     * 根据SKU ID查询所有仓库的库存
     */
    List<Stock> findBySkuId(Long skuId);

    /**
     * 根据仓库ID查询所有库存
     */
    List<Stock> findByWarehouseId(Long warehouseId);

    /**
     * 根据款号编码查询库存
     */
    List<Stock> findByStyleCode(String styleCode);

    /**
     * 查询低于安全库存的库存
     */
    List<Stock> findBelowSafetyStock();

    /**
     * 根据仓库ID和SKU ID列表批量查询库存
     */
    List<Stock> findByWarehouseIdAndSkuIds(Long warehouseId, List<Long> skuIds);
}