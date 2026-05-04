package com.garment.order.domain.sales.event;

import com.garment.common.domain.DomainEvent;
import lombok.Getter;

/**
 * 销售单完成事件
 * <p>
 * 当销售单从已发货状态完成为已完成状态时发布此事件。
 * 其他服务可以监听此事件执行后续操作，例如：
 * payment-service 结算款项、report-service 生成销售报表等。
 * </p>
 *
 * @author garment-factory-saas
 */
@Getter
public class OrderCompletedEvent extends DomainEvent {

    /** 销售单ID */
    private final Long orderId;

    /** 销售单号 */
    private final String orderNo;

    /** 租户ID */
    private final Long tenantId;

    /**
     * 构造销售单完成事件
     *
     * @param orderId   销售单ID
     * @param orderNo   销售单号
     * @param tenantId  租户ID
     */
    public OrderCompletedEvent(Long orderId, String orderNo, Long tenantId) {
        super();
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.tenantId = tenantId;
    }
}