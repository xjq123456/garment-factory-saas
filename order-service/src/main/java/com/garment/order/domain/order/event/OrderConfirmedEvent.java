package com.garment.order.domain.order.event;

import com.garment.common.domain.DomainEvent;
import lombok.Getter;

/**
 * 加工单确认事件
 * <p>
 * 当加工单从草稿状态确认为已确认状态时发布此事件。
 * 其他服务可以监听此事件执行后续操作，例如：
 * inventory-service 预占库存、production-service 创建生产计划等。
 * </p>
 *
 * @author garment-factory-saas
 */
@Getter
public class OrderConfirmedEvent extends DomainEvent {

    /** 加工单ID */
    private final Long orderId;

    /** 加工单号 */
    private final String orderNo;

    /** 租户ID */
    private final Long tenantId;

    /**
     * 构造加工单确认事件
     *
     * @param orderId   加工单ID
     * @param orderNo   加工单号
     * @param tenantId  租户ID
     */
    public OrderConfirmedEvent(Long orderId, String orderNo, Long tenantId) {
        super();
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.tenantId = tenantId;
    }
}