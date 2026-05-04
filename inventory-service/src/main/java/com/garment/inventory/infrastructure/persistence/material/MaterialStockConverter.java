package com.garment.inventory.infrastructure.persistence.material;

import com.garment.inventory.domain.material.entity.MaterialStock;
import com.garment.inventory.domain.material.vo.MaterialType;
import org.springframework.stereotype.Component;

/**
 * 原材料库存对象转换器
 */
@Component
public class MaterialStockConverter {

    public MaterialStockDO toDO(MaterialStock entity) {
        if (entity == null) {
            return null;
        }
        MaterialStockDO DO = new MaterialStockDO();
        DO.setId(entity.getId());
        DO.setTenantId(entity.getTenantId());
        DO.setWarehouseId(entity.getWarehouseId());
        DO.setMaterialId(entity.getMaterialId());
        DO.setMaterialCode(entity.getMaterialCode());
        DO.setMaterialName(entity.getMaterialName());
        DO.setMaterialType(entity.getMaterialType() != null ? entity.getMaterialType().getCode() : null);
        DO.setTotalQty(entity.getTotalQty());
        DO.setAvailableQty(entity.getAvailableQty());
        DO.setLockedQty(entity.getLockedQty());
        DO.setSafetyStock(entity.getSafetyStock());
        DO.setUnit(entity.getUnit());
        DO.setBatchNo(entity.getBatchNo());
        DO.setRemark(entity.getRemark());
        DO.setCreateBy(entity.getCreateBy());
        DO.setUpdateBy(entity.getUpdateBy());
        DO.setCreateTime(entity.getCreateTime());
        DO.setUpdateTime(entity.getUpdateTime());
        return DO;
    }

    public MaterialStock toEntity(MaterialStockDO DO) {
        if (DO == null) {
            return null;
        }
        MaterialStock entity = new MaterialStock();
        entity.setId(DO.getId());
        entity.setTenantId(DO.getTenantId());
        entity.setWarehouseId(DO.getWarehouseId());
        entity.setMaterialId(DO.getMaterialId());
        entity.setMaterialCode(DO.getMaterialCode());
        entity.setMaterialName(DO.getMaterialName());
        entity.setMaterialType(DO.getMaterialType() != null ? MaterialType.of(DO.getMaterialType()) : null);
        entity.setTotalQty(DO.getTotalQty());
        entity.setAvailableQty(DO.getAvailableQty());
        entity.setLockedQty(DO.getLockedQty());
        entity.setSafetyStock(DO.getSafetyStock());
        entity.setUnit(DO.getUnit());
        entity.setBatchNo(DO.getBatchNo());
        entity.setRemark(DO.getRemark());
        entity.setCreateBy(DO.getCreateBy());
        entity.setUpdateBy(DO.getUpdateBy());
        entity.setCreateTime(DO.getCreateTime());
        entity.setUpdateTime(DO.getUpdateTime());
        return entity;
    }
}