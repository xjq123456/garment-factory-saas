package com.garment.production.domain.order.entity;

import com.garment.common.domain.AggregateRoot;
import com.garment.production.domain.order.vo.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 生产工单 - 聚合根
 *
 * 管理服装生产的完整生命周期，从创建工单到生产完成。
 * 关联款式(style)信息，追踪生产数量和状态。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ProductionOrder extends AggregateRoot {

    /** 工单编号 */
    private String orderNo;

    /** 工艺路线ID */
    private Long routeId;

    /** 款式ID (关联style-service) */
    private Long styleId;

    /** 款式编号 */
    private String styleCode;

    /** 款式名称 */
    private String styleName;

    /** SKU ID */
    private Long skuId;

    /** SKU编号 */
    private String skuCode;

    /** 客户名称 */
    private String customerName;

    /** 计划生产总数 */
    private Integer totalQty;

    /** 已完成数量 */
    private Integer completedQty;

    /** 不良品数量 */
    private Integer defectiveQty;

    /** 单位 */
    private String unit;

    /** 交货日期 */
    private LocalDate deliveryDate;

    /** 计划开始日期 */
    private LocalDate startDate;

    /** 计划结束日期 */
    private LocalDate endDate;

    /** 优先级: 0-普通 1-加急 2-特急 */
    private Integer priority;

    /** 工单状态 */
    private OrderStatus status;

    /** 备注 */
    private String remark;

    /**
     * 创建生产工单
     */
    public static ProductionOrder create(String orderNo, Long styleId, String styleCode,
                                         String styleName, Integer totalQty, String unit) {
        ProductionOrder order = new ProductionOrder();
        order.setOrderNo(orderNo);
        order.setStyleId(styleId);
        order.setStyleCode(styleCode);
        order.setStyleName(styleName);
        order.setTotalQty(totalQty);
        order.setCompletedQty(0);
        order.setDefectiveQty(0);
        order.setUnit(unit != null ? unit : "件");
        order.setPriority(0);
        order.setStatus(OrderStatus.CREATED);
        return order;
    }

    /**
     * 审批工单
     */
    public void approve() {
        this.status.validateTransition(OrderStatus.APPROVED);
        this.status = OrderStatus.APPROVED;
    }

    /**
     * 开始生产
     */
    public void startProduction() {
        this.status.validateTransition(OrderStatus.IN_PROGRESS);
        this.status = OrderStatus.IN_PROGRESS;
    }

    /**
     * 暂停生产
     */
    public void suspend() {
        this.status.validateTransition(OrderStatus.SUSPENDED);
        this.status = OrderStatus.SUSPENDED;
    }

    /**
     * 恢复生产
     */
    public void resume() {
        this.status.validateTransition(OrderStatus.IN_PROGRESS);
        this.status = OrderStatus.IN_PROGRESS;
    }

    /**
     * 完成工单
     */
    public void complete() {
        this.status.validateTransition(OrderStatus.COMPLETED);
        this.status = OrderStatus.COMPLETED;
    }

    /**
     * 关闭工单
     */
    public void close() {
        this.status.validateTransition(OrderStatus.CLOSED);
        this.status = OrderStatus.CLOSED;
    }

    /**
     * 更新生产数量（报工时调用）
     */
    public void addProduction(int qualifiedQty, int defectiveQty) {
        if (this.status != OrderStatus.IN_PROGRESS) {
            throw new IllegalStateException("只有生产中的工单才能报工");
        }
        this.completedQty += qualifiedQty;
        this.defectiveQty += defectiveQty;

        // 自动完成判断
        if (this.completedQty + this.defectiveQty >= this.totalQty) {
            this.status = OrderStatus.COMPLETED;
        }
    }

    /**
     * 获取生产完成百分比
     */
    public BigDecimal getCompletionRate() {
        if (totalQty == null || totalQty == 0) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(completedQty)
                .divide(new BigDecimal(totalQty), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100));
    }

    /**
     * 是否已逾期
     */
    public boolean isOverdue() {
        return deliveryDate != null && LocalDate.now().isAfter(deliveryDate)
                && status != OrderStatus.COMPLETED && status != OrderStatus.CLOSED;
    }
}