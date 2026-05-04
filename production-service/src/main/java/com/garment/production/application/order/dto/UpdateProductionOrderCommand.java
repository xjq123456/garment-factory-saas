package com.garment.production.application.order.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 更新生产工单命令
 */
@Data
public class UpdateProductionOrderCommand {

    private String customerName;

    private Integer totalQty;

    private String unit;

    private LocalDate deliveryDate;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer priority;

    private String remark;
}