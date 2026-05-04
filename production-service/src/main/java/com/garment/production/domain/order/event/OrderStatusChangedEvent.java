package com.garment.production.domain.order.event;

import com.garment.common.domain.DomainEvent;
import com.garment.production.domain.order.vo.OrderStatus;
import lombok.Getter;

/**
 * 生产工单状态变更事件
 */
@Getter
public class OrderStatusChangedEvent extends DomainEvent {

    private final Long orderId;
    private final String orderNo;
    private final OrderStatus oldStatus;
    private final OrderStatus newStatus;

    public OrderStatusChangedEvent(Long orderId, String orderNo,
                                   OrderStatus oldStatus, OrderStatus newStatus) {
        super("OrderStatusChanged");
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }
}