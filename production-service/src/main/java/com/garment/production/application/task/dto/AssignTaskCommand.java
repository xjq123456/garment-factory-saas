package com.garment.production.application.task.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 分配生产任务命令
 */
@Data
public class AssignTaskCommand {

    @NotNull(message = "任务ID不能为空")
    private Long taskId;

    @NotNull(message = "工位ID不能为空")
    private Long stationId;

    private Long workerId;

    private String workerName;
}