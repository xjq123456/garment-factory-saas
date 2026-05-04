package com.garment.production.domain.task.event;

import com.garment.common.domain.DomainEvent;
import lombok.Getter;

/**
 * 生产任务完成事件
 */
@Getter
public class TaskCompletedEvent extends DomainEvent {

    private final Long taskId;
    private final String taskNo;
    private final Long orderId;
    private final Long stepId;
    private final int completedQty;
    private final int defectiveQty;

    public TaskCompletedEvent(Long taskId, String taskNo, Long orderId,
                              Long stepId, int completedQty, int defectiveQty) {
        super("TaskCompleted");
        this.taskId = taskId;
        this.taskNo = taskNo;
        this.orderId = orderId;
        this.stepId = stepId;
        this.completedQty = completedQty;
        this.defectiveQty = defectiveQty;
    }
}