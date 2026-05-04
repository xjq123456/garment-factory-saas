package com.garment.inventory.domain.warehouse.repository;

import com.garment.inventory.domain.warehouse.entity.Warehouse;

import java.util.List;
import java.util.Optional;

/**
 * 仓储仓储接口
 */
public interface WarehouseRepository {

    /**
     * 保存仓库
     */
    void save(Warehouse warehouse);

    /**
     * 更新仓库
     */
    void update(Warehouse warehouse);

    /**
     * 根据ID查找仓库
     */
    Optional<Warehouse> findById(Long id);

    /**
     * 根据仓库编码查找仓库
     */
    Optional<Warehouse> findByCode(String code);

    /**
     * 查询所有仓库
     */
    List<Warehouse> findAll();

    /**
     * 根据仓库类型查询仓库
     */
    List<Warehouse> findByType(int type);
}