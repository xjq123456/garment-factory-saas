package com.garment.inventory.domain.material.repository;

import com.garment.inventory.domain.material.entity.MaterialStock;

import java.util.List;
import java.util.Optional;

/**
 * 原材料库存仓储接口
 */
public interface MaterialStockRepository {

    /**
     * 保存库存
     */
    void save(MaterialStock stock);

    /**
     * 更新库存
     */
    void update(MaterialStock stock);

    /**
     * 根据ID查找库存
     */
    Optional<MaterialStock> findById(Long id);

    /**
     * 根据仓库ID和原材料ID查找库存
     */
    Optional<MaterialStock> findByWarehouseIdAndMaterialId(Long warehouseId, Long materialId);

    /**
     * 根据仓库ID、原材料ID和批次号查找库存
     */
    Optional<MaterialStock> findByWarehouseIdAndMaterialIdAndBatchNo(Long warehouseId, Long materialId, String batchNo);

    /**
     * 根据原材料ID查询所有仓库的库存
     */
    List<MaterialStock> findByMaterialId(Long materialId);

    /**
     * 根据仓库ID查询所有库存
     */
    List<MaterialStock> findByWarehouseId(Long warehouseId);

    /**
     * 查询低于安全库存的库存
     */
    List<MaterialStock> findBelowSafetyStock();
}