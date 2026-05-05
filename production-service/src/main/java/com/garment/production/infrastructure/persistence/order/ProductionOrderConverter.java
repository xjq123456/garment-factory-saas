package com.garment.production.infrastructure.persistence.order;

import com.garment.production.domain.order.entity.ProductionOrder;
import com.garment.production.domain.order.vo.OrderStatus;
import org.springframework.stereotype.Component;

/**
 * 生产工单 DO ↔ Entity 转换器
 */
@Component
public class ProductionOrderConverter {

    public ProductionOrderDO toDO(ProductionOrder entity) {
        if (entity == null) return null;
        ProductionOrderDO DO = new ProductionOrderDO();
        DO.setId(entity.getId());
        DO.setTenantId(entity.getTenantId());
        DO.setOrderNo(entity.getOrderNo());
        DO.setRouteId(entity.getRouteId());
        DO.setStyleId(entity.getStyleId());
        DO.setStyleCode(entity.getStyleCode());
        DO.setStyleName(entity.getStyleName());
        DO.setSkuId(entity.getSkuId());
        DO.setSkuCode(entity.getSkuCode());
        DO.setCustomerName(entity.getCustomerName());
        DO.setTotalQty(entity.getTotalQty());
        DO.setCompletedQty(entity.getCompletedQty());
        DO.setDefectiveQty(entity.getDefectiveQty());
        DO.setUnit(entity.getUnit());
        DO.setDeliveryDate(entity.getDeliveryDate());
        DO.setStartDate(entity.getStartDate());
        DO.setEndDate(entity.getEndDate());
        DO.setPriority(entity.getPriority());
        DO.setStatus(entity.getStatus() != null ? entity.getStatus().getCode() : null);
        DO.setRemark(entity.getRemark());
        DO.setCreateBy(entity.getCreateBy());
        DO.setCreateTime(entity.getCreateTime());
        DO.setUpdateBy(entity.getUpdateBy());
        DO.setUpdateTime(entity.getUpdateTime());
        DO.setVersion(entity.getVersion());
        return DO;
    }

    public ProductionOrder toEntity(ProductionOrderDO DO) {
        if (DO == null) return null;
        ProductionOrder entity = new ProductionOrder();
        entity.setId(DO.getId());
        entity.setTenantId(DO.getTenantId());
        entity.setOrderNo(DO.getOrderNo());
        entity.setRouteId(DO.getRouteId());
        entity.setStyleId(DO.getStyleId());
        entity.setStyleCode(DO.getStyleCode());
        entity.setStyleName(DO.getStyleName());
        entity.setSkuId(DO.getSkuId());
        entity.setSkuCode(DO.getSkuCode());
        entity.setCustomerName(DO.getCustomerName());
        entity.setTotalQty(DO.getTotalQty());
        entity.setCompletedQty(DO.getCompletedQty());
        entity.setDefectiveQty(DO.getDefectiveQty());
        entity.setUnit(DO.getUnit());
        entity.setDeliveryDate(DO.getDeliveryDate());
        entity.setStartDate(DO.getStartDate());
        entity.setEndDate(DO.getEndDate());
        entity.setPriority(DO.getPriority());
        entity.setStatus(DO.getStatus() != null ? OrderStatus.valueOf(DO.getStatus()) : null);
        entity.setRemark(DO.getRemark());
        entity.setCreateBy(DO.getCreateBy());
        entity.setCreateTime(DO.getCreateTime());
        entity.setUpdateBy(DO.getUpdateBy());
        entity.setUpdateTime(DO.getUpdateTime());
        entity.setVersion(DO.getVersion());
        return entity;
    }
}