package com.garment.production.application.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 创建生产工单命令
 */
@Data
public class CreateProductionOrderCommand {

    @NotNull(message = "款式ID不能为空")
    private Long styleId;

    @NotBlank(message = "款式编号不能为空")
    private String styleCode;

    @NotBlank(message = "款式名称不能为空")
    private String styleName;

    private Long skuId;

    private String skuCode;

    private String customerName;

    @NotNull(message = "计划数量不能为空")
    @Min(value = 1, message = "计划数量必须大于0")
    private Integer totalQty;

    private String unit;

    private LocalDate deliveryDate;

    private LocalDate startDate;

    private LocalDate endDate;

    /** 优先级: 0-普通 1-加急 2-特急 */
    private Integer priority;

    private String remark;
}