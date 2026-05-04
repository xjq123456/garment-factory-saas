package com.garment.production.domain.order.event;

import com.garment.common.domain.DomainEvent;
import lombok.Getter;

/**
 * 生产工单创建事件
 */
@Getter
public class OrderCreatedEvent extends DomainEvent {

    private final Long orderId;
    private final String orderNo;
    private final Long styleId;
    private final Integer totalQty;

    public OrderCreatedEvent(Long orderId, String orderNo, Long styleId, Integer totalQty) {
        super("OrderCreated");
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.styleId = styleId;
        this.totalQty = totalQty;
    }
}