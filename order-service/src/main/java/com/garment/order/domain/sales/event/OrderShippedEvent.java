package com.garment.order.domain.sales.event;

import com.garment.common.domain.DomainEvent;
import lombok.Getter;

/**
 * 销售单发货事件
 * <p>
 * 当销售单从已确认状态发货为已发货状态时发布此事件。
 * 其他服务可以监听此事件执行后续操作，例如：
 * inventory-service 扣减库存、logistics-service 创建物流单等。
 * </p>
 *
 * @author garment-factory-saas
 */
@Getter
public class OrderShippedEvent extends DomainEvent {

    /** 销售单ID */
    private final Long orderId;

    /** 销售单号 */
    private final String orderNo;

    /** 租户ID */
    private final Long tenantId;

    /**
     * 构造销售单发货事件
     *
     * @param orderId   销售单ID
     * @param orderNo   销售单号
     * @param tenantId  租户ID
     */
    public OrderShippedEvent(Long orderId, String orderNo, Long tenantId) {
        super();
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.tenantId = tenantId;
    }
}