package com.garment.order.infrastructure.persistence.order;

import com.garment.order.domain.order.entity.ProductionOrder;
import com.garment.order.domain.order.entity.ProductionOrderItem;
import java.util.List;

public final class ProductionOrderConverter {
    private ProductionOrderConverter() {}

    public static ProductionOrderDO toDO(ProductionOrder order) {
        ProductionOrderDO d = new ProductionOrderDO();
        d.setId(order.getId());
        d.setTenantId(order.getTenantId());
        d.setOrderNo(order.getOrderNo());
        d.setCustomerId(order.getCustomerId());
        d.setCustomerName(order.getCustomerName());
        d.setStyleId(order.getStyleId());
        d.setStyleName(order.getStyleName());
        d.setTotalQuantity(order.getTotalQuantity());
        d.setUnit(order.getUnit());
        d.setUnitPrice(order.getUnitPrice());
        d.setTotalAmount(order.getTotalAmount());
        d.setDeliveryDate(order.getDeliveryDate());
        d.setStatus(order.getStatus());
        d.setRemark(order.getRemark());
        d.setCreatedBy(order.getCreatedBy());
        d.setUpdatedBy(order.getUpdatedBy());
        return d;
    }

    public static ProductionOrder toDomain(ProductionOrderDO d) {
        ProductionOrder o = new ProductionOrder(d.getId(), d.getTenantId(), d.getOrderNo());
        o.setCustomerId(d.getCustomerId());
        o.setCustomerName(d.getCustomerName());
        o.setStyleId(d.getStyleId());
        o.setStyleName(d.getStyleName());
        o.setTotalQuantity(d.getTotalQuantity());
        o.setUnit(d.getUnit());
        o.setUnitPrice(d.getUnitPrice());
        o.setTotalAmount(d.getTotalAmount());
        o.setDeliveryDate(d.getDeliveryDate());
        o.setStatus(d.getStatus());
        o.setRemark(d.getRemark());
        o.setCreatedBy(d.getCreatedBy());
        o.setCreatedAt(d.getCreatedAt());
        o.setUpdatedBy(d.getUpdatedBy());
        o.setUpdatedAt(d.getUpdatedAt());
        return o;
    }

    public static ProductionOrderItemDO toItemDO(ProductionOrderItem item) {
        ProductionOrderItemDO d = new ProductionOrderItemDO();
        d.setId(item.getId());
        d.setOrderId(item.getOrderId());
        d.setSkuId(item.getSkuId());
        d.setSkuCode(item.getSkuCode());
        d.setColor(item.getColor());
        d.setSize(item.getSize());
        d.setQuantity(item.getQuantity());
        d.setCompletedQuantity(item.getCompletedQuantity());
        d.setRemark(item.getRemark());
        return d;
    }

    public static ProductionOrderItem toItemDomain(ProductionOrderItemDO d) {
        ProductionOrderItem i = new ProductionOrderItem(d.getId(), d.getOrderId());
        i.setSkuId(d.getSkuId());
        i.setSkuCode(d.getSkuCode());
        i.setColor(d.getColor());
        i.setSize(d.getSize());
        i.setQuantity(d.getQuantity());
        i.setCompletedQuantity(d.getCompletedQuantity());
        i.setRemark(d.getRemark());
        return i;
    }
}
