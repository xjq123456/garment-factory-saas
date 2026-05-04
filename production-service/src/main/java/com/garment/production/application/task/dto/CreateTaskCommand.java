package com.garment.production.application.task.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 创建生产任务命令
 */
@Data
public class CreateTaskCommand {

    @NotNull(message = "工单ID不能为空")
    private Long orderId;

    private Long routeId;

    @NotNull(message = "工序步骤ID不能为空")
    private Long stepId;

    @NotBlank(message = "工序名称不能为空")
    private String stepName;

    @NotNull(message = "计划数量不能为空")
    @Min(value = 1, message = "计划数量必须大于0")
    private Integer planQty;

    private LocalDateTime planStartTime;

    private LocalDateTime planEndTime;

    private Integer priority;

    private String remark;
}