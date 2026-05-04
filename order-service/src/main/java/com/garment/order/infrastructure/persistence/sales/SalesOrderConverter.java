package com.garment.order.infrastructure.persistence.sales;

import com.garment.order.domain.sales.entity.SalesOrder;
import com.garment.order.domain.sales.entity.SalesOrderItem;

public final class SalesOrderConverter {
    private SalesOrderConverter() {}

    public static SalesOrderDO toDO(SalesOrder order) {
        SalesOrderDO d = new SalesOrderDO();
        d.setId(order.getId());
        d.setTenantId(order.getTenantId());
        d.setOrderNo(order.getOrderNo());
        d.setCustomerId(order.getCustomerId());
        d.setCustomerName(order.getCustomerName());
        d.setTotalAmount(order.getTotalAmount());
        d.setPaidAmount(order.getPaidAmount());
        d.setDeliveryDate(order.getDeliveryDate());
        d.setStatus(order.getStatus());
        d.setShippingAddress(order.getShippingAddress());
        d.setRemark(order.getRemark());
        d.setCreatedBy(order.getCreatedBy());
        d.setUpdatedBy(order.getUpdatedBy());
        return d;
    }

    public static SalesOrder toDomain(SalesOrderDO d) {
        SalesOrder o = new SalesOrder(d.getId(), d.getTenantId(), d.getOrderNo());
        o.setCustomerId(d.getCustomerId());
        o.setCustomerName(d.getCustomerName());
        o.setTotalAmount(d.getTotalAmount());
        o.setPaidAmount(d.getPaidAmount());
        o.setDeliveryDate(d.getDeliveryDate());
        o.setStatus(d.getStatus());
        o.setShippingAddress(d.getShippingAddress());
        o.setRemark(d.getRemark());
        o.setCreatedBy(d.getCreatedBy());
        o.setCreatedAt(d.getCreatedAt());
        o.setUpdatedBy(d.getUpdatedBy());
        o.setUpdatedAt(d.getUpdatedAt());
        return o;
    }

    public static SalesOrderItemDO toItemDO(SalesOrderItem item) {
        SalesOrderItemDO d = new SalesOrderItemDO();
        d.setId(item.getId());
        d.setOrderId(item.getOrderId());
        d.setSkuId(item.getSkuId());
        d.setSkuCode(item.getSkuCode());
        d.setColor(item.getColor());
        d.setSize(item.getSize());
        d.setQuantity(item.getQuantity());
        d.setUnitPrice(item.getUnitPrice());
        d.setAmount(item.getAmount());
        d.setRemark(item.getRemark());
        return d;
    }

    public static SalesOrderItem toItemDomain(SalesOrderItemDO d) {
        SalesOrderItem i = new SalesOrderItem(d.getId(), d.getOrderId());
        i.setSkuId(d.getSkuId());
        i.setSkuCode(d.getSkuCode());
        i.setColor(d.getColor());
        i.setSize(d.getSize());
        i.setQuantity(d.getQuantity());
        i.setUnitPrice(d.getUnitPrice());
        i.setAmount(d.getAmount());
        i.setRemark(d.getRemark());
        return i;
    }
}