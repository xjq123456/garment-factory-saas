package com.garment.inventory.infrastructure.persistence.stocklog;

import com.garment.inventory.domain.stocklog.entity.StockLog;
import com.garment.inventory.domain.stocklog.vo.ChangeType;
import org.springframework.stereotype.Component;

/**
 * 成品库存变动日志对象转换器
 */
@Component
public class StockLogConverter {

    public StockLogDO toDO(StockLog entity) {
        if (entity == null) {
            return null;
        }
        StockLogDO DO = new StockLogDO();
        DO.setId(entity.getId());
        DO.setTenantId(entity.getTenantId());
        DO.setWarehouseId(entity.getWarehouseId());
        DO.setSkuId(entity.getSkuId());
        DO.setChangeType(entity.getChangeType() != null ? entity.getChangeType().getCode() : null);
        DO.setChangeQty(entity.getChangeQty());
        DO.setBeforeQty(entity.getBeforeQty());
        DO.setAfterQty(entity.getAfterQty());
        DO.setBizType(entity.getBizType());
        DO.setBizNo(entity.getBizNo());
        DO.setOperatorId(entity.getOperatorId());
        DO.setOperatorName(entity.getOperatorName());
        DO.setRemark(entity.getRemark());
        DO.setCreateTime(entity.getCreateTime());
        return DO;
    }

    public StockLog toEntity(StockLogDO DO) {
        if (DO == null) {
            return null;
        }
        StockLog entity = new StockLog();
        entity.setId(DO.getId());
        entity.setTenantId(DO.getTenantId());
        entity.setWarehouseId(DO.getWarehouseId());
        entity.setSkuId(DO.getSkuId());
        entity.setChangeType(DO.getChangeType() != null ? ChangeType.of(DO.getChangeType()) : null);
        entity.setChangeQty(DO.getChangeQty());
        entity.setBeforeQty(DO.getBeforeQty());
        entity.setAfterQty(DO.getAfterQty());
        entity.setBizType(DO.getBizType());
        entity.setBizNo(DO.getBizNo());
        entity.setOperatorId(DO.getOperatorId());
        entity.setOperatorName(DO.getOperatorName());
        entity.setRemark(DO.getRemark());
        entity.setCreateTime(DO.getCreateTime());
        return entity;
    }
}