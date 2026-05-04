package com.garment.inventory.infrastructure.persistence.stock;

import com.garment.inventory.domain.stock.entity.Stock;
import org.springframework.stereotype.Component;

/**
 * 成品库存对象转换器
 */
@Component
public class StockConverter {

    public StockDO toDO(Stock entity) {
        if (entity == null) {
            return null;
        }
        StockDO DO = new StockDO();
        DO.setId(entity.getId());
        DO.setTenantId(entity.getTenantId());
        DO.setWarehouseId(entity.getWarehouseId());
        DO.setSkuId(entity.getSkuId());
        DO.setStyleId(entity.getStyleId());
        DO.setStyleCode(entity.getStyleCode());
        DO.setColor(entity.getColor());
        DO.setSize(entity.getSize());
        DO.setTotalQty(entity.getTotalQty());
        DO.setAvailableQty(entity.getAvailableQty());
        DO.setLockedQty(entity.getLockedQty());
        DO.setSafetyStock(entity.getSafetyStock());
        DO.setUnit(entity.getUnit());
        DO.setRemark(entity.getRemark());
        DO.setCreateBy(entity.getCreateBy());
        DO.setUpdateBy(entity.getUpdateBy());
        DO.setCreateTime(entity.getCreateTime());
        DO.setUpdateTime(entity.getUpdateTime());
        return DO;
    }

    public Stock toEntity(StockDO DO) {
        if (DO == null) {
            return null;
        }
        Stock entity = new Stock();
        entity.setId(DO.getId());
        entity.setTenantId(DO.getTenantId());
        entity.setWarehouseId(DO.getWarehouseId());
        entity.setSkuId(DO.getSkuId());
        entity.setStyleId(DO.getStyleId());
        entity.setStyleCode(DO.getStyleCode());
        entity.setColor(DO.getColor());
        entity.setSize(DO.getSize());
        entity.setTotalQty(DO.getTotalQty());
        entity.setAvailableQty(DO.getAvailableQty());
        entity.setLockedQty(DO.getLockedQty());
        entity.setSafetyStock(DO.getSafetyStock());
        entity.setUnit(DO.getUnit());
        entity.setRemark(DO.getRemark());
        entity.setCreateBy(DO.getCreateBy());
        entity.setUpdateBy(DO.getUpdateBy());
        entity.setCreateTime(DO.getCreateTime());
        entity.setUpdateTime(DO.getUpdateTime());
        return entity;
    }
}