package com.garment.production.application.order.dto;

import com.garment.production.domain.order.vo.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 生产工单视图对象
 */
@Data
public class ProductionOrderVO {

    private Long id;
    private String orderNo;
    private Long styleId;
    private String styleCode;
    private String styleName;
    private Long skuId;
    private String skuCode;
    private String customerName;
    private Integer totalQty;
    private Integer completedQty;
    private Integer defectiveQty;
    private String unit;
    private LocalDate deliveryDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer priority;
    private OrderStatus status;
    private BigDecimal completionRate;
    private boolean overdue;
    private String remark;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
}