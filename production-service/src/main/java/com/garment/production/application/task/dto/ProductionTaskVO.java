package com.garment.production.application.task.dto;

import com.garment.production.domain.task.vo.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 生产任务视图对象
 */
@Data
public class ProductionTaskVO {

    private Long id;
    private String taskNo;
    private Long orderId;
    private Long routeId;
    private Long stepId;
    private String stepName;
    private Long stationId;
    private String stationName;
    private Long workerId;
    private String workerName;
    private Integer planQty;
    private Integer completedQty;
    private Integer defectiveQty;
    private String unit;
    private LocalDateTime planStartTime;
    private LocalDateTime planEndTime;
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
    private TaskStatus status;
    private Integer priority;
    private Integer completionPercentage;
    private boolean overdue;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}