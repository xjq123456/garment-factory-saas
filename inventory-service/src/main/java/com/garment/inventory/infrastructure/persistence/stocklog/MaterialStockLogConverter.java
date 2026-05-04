package com.garment.inventory.infrastructure.persistence.stocklog;

import com.garment.inventory.domain.stocklog.entity.MaterialStockLog;
import com.garment.inventory.domain.stocklog.vo.ChangeType;
import org.springframework.stereotype.Component;

/**
 * 原材料库存变动日志对象转换器
 */
@Component
public class MaterialStockLogConverter {

    public MaterialStockLogDO toDO(MaterialStockLog entity) {
        if (entity == null) {
            return null;
        }
        MaterialStockLogDO DO = new MaterialStockLogDO();
        DO.setId(entity.getId());
        DO.setTenantId(entity.getTenantId());
        DO.setWarehouseId(entity.getWarehouseId());
        DO.setMaterialId(entity.getMaterialId());
        DO.setChangeType(entity.getChangeType() != null ? entity.getChangeType().getCode() : null);
        DO.setChangeQty(entity.getChangeQty());
        DO.setBeforeQty(entity.getBeforeQty());
        DO.setAfterQty(entity.getAfterQty());
        DO.setBizType(entity.getBizType());
        DO.setBizNo(entity.getBizNo());
        DO.setBatchNo(entity.getBatchNo());
        DO.setOperatorId(entity.getOperatorId());
        DO.setOperatorName(entity.getOperatorName());
        DO.setRemark(entity.getRemark());
        DO.setCreateTime(entity.getCreateTime());
        return DO;
    }

    public MaterialStockLog toEntity(MaterialStockLogDO DO) {
        if (DO == null) {
            return null;
        }
        MaterialStockLog entity = new MaterialStockLog();
        entity.setId(DO.getId());
        entity.setTenantId(DO.getTenantId());
        entity.setWarehouseId(DO.getWarehouseId());
        entity.setMaterialId(DO.getMaterialId());
        entity.setChangeType(DO.getChangeType() != null ? ChangeType.of(DO.getChangeType()) : null);
        entity.setChangeQty(DO.getChangeQty());
        entity.setBeforeQty(DO.getBeforeQty());
        entity.setAfterQty(DO.getAfterQty());
        entity.setBizType(DO.getBizType());
        entity.setBizNo(DO.getBizNo());
        entity.setBatchNo(DO.getBatchNo());
        entity.setOperatorId(DO.getOperatorId());
        entity.setOperatorName(DO.getOperatorName());
        entity.setRemark(DO.getRemark());
        entity.setCreateTime(DO.getCreateTime());
        return entity;
    }
}