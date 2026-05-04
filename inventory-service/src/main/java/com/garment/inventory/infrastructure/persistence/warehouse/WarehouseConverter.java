package com.garment.inventory.infrastructure.persistence.warehouse;

import com.garment.inventory.domain.warehouse.entity.Warehouse;
import com.garment.inventory.domain.warehouse.vo.WarehouseStatus;
import com.garment.inventory.domain.warehouse.vo.WarehouseType;
import org.springframework.stereotype.Component;

/**
 * 仓库对象转换器
 */
@Component
public class WarehouseConverter {

    public WarehouseDO toDO(Warehouse entity) {
        if (entity == null) {
            return null;
        }
        WarehouseDO DO = new WarehouseDO();
        DO.setId(entity.getId());
        DO.setTenantId(entity.getTenantId());
        DO.setWarehouseCode(entity.getWarehouseCode());
        DO.setWarehouseName(entity.getWarehouseName());
        DO.setWarehouseType(entity.getWarehouseType() != null ? entity.getWarehouseType().getCode() : null);
        DO.setContactPerson(entity.getContactPerson());
        DO.setContactPhone(entity.getContactPhone());
        DO.setAddress(entity.getAddress());
        DO.setStatus(entity.getStatus() != null ? entity.getStatus().getCode() : null);
        DO.setRemark(entity.getRemark());
        DO.setCreateBy(entity.getCreateBy());
        DO.setUpdateBy(entity.getUpdateBy());
        DO.setCreateTime(entity.getCreateTime());
        DO.setUpdateTime(entity.getUpdateTime());
        return DO;
    }

    public Warehouse toEntity(WarehouseDO DO) {
        if (DO == null) {
            return null;
        }
        Warehouse entity = new Warehouse();
        entity.setId(DO.getId());
        entity.setTenantId(DO.getTenantId());
        entity.setWarehouseCode(DO.getWarehouseCode());
        entity.setWarehouseName(DO.getWarehouseName());
        entity.setWarehouseType(DO.getWarehouseType() != null ? WarehouseType.of(DO.getWarehouseType()) : null);
        entity.setContactPerson(DO.getContactPerson());
        entity.setContactPhone(DO.getContactPhone());
        entity.setAddress(DO.getAddress());
        entity.setStatus(DO.getStatus() != null ? WarehouseStatus.of(DO.getStatus()) : null);
        entity.setRemark(DO.getRemark());
        entity.setCreateBy(DO.getCreateBy());
        entity.setUpdateBy(DO.getUpdateBy());
        entity.setCreateTime(DO.getCreateTime());
        entity.setUpdateTime(DO.getUpdateTime());
        return entity;
    }
}