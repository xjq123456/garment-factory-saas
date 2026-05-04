package com.garment.production.application.task.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 报工命令
 */
@Data
public class ReportWorkCommand {

    @NotNull(message = "任务ID不能为空")
    private Long taskId;

    @NotNull(message = "报工数量不能为空")
    @Min(value = 1, message = "报工数量必须大于0")
    private Integer reportQty;

    @NotNull(message = "合格数量不能为空")
    @Min(value = 0, message = "合格数量不能为负")
    private Integer qualifiedQty;

    @NotNull(message = "不良品数量不能为空")
    @Min(value = 0, message = "不良品数量不能为负")
    private Integer defectiveQty;

    /** 工时(小时) */
    private BigDecimal workHours;

    private Long stationId;

    private String remark;
}