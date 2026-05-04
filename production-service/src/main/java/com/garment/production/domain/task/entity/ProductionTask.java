package com.garment.production.domain.task.entity;

import com.garment.common.domain.AggregateRoot;
import com.garment.production.domain.task.vo.TaskStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 生产任务 - 聚合根
 *
 * 将生产工单分解为具体可执行的工序任务，分配到工位和工人。
 * 任务跟随工单的工艺路线，每个工序对应一个或多个任务。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ProductionTask extends AggregateRoot {

    /** 任务编号 */
    private String taskNo;

    /** 生产工单ID */
    private Long orderId;

    /** 工艺路线ID */
    private Long routeId;

    /** 工序步骤ID */
    private Long stepId;

    /** 工序名称 */
    private String stepName;

    /** 分配工位ID */
    private Long stationId;

    /** 工位名称 */
    private String stationName;

    /** 工人ID */
    private Long workerId;

    /** 工人姓名 */
    private String workerName;

    /** 计划数量 */
    private Integer planQty;

    /** 完成数量 */
    private Integer completedQty;

    /** 不良品数量 */
    private Integer defectiveQty;

    /** 单位 */
    private String unit;

    /** 计划开始时间 */
    private LocalDateTime planStartTime;

    /** 计划结束时间 */
    private LocalDateTime planEndTime;

    /** 实际开始时间 */
    private LocalDateTime actualStartTime;

    /** 实际结束时间 */
    private LocalDateTime actualEndTime;

    /** 任务状态 */
    private TaskStatus status;

    /** 优先级: 0-普通 1-加急 2-特急 */
    private Integer priority;

    /** 备注 */
    private String remark;

    /**
     * 创建生产任务
     */
    public static ProductionTask create(String taskNo, Long orderId, Long stepId,
                                        String stepName, Integer planQty) {
        ProductionTask task = new ProductionTask();
        task.setTaskNo(taskNo);
        task.setOrderId(orderId);
        task.setStepId(stepId);
        task.setStepName(stepName);
        task.setPlanQty(planQty);
        task.setCompletedQty(0);
        task.setDefectiveQty(0);
        task.setUnit("件");
        task.setPriority(0);
        task.setStatus(TaskStatus.PENDING);
        return task;
    }

    /**
     * 分配任务到工位和工人
     */
    public void assign(Long stationId, String stationName, Long workerId, String workerName) {
        if (this.status != TaskStatus.PENDING) {
            throw new IllegalStateException("只有待分配的任务才能进行分配");
        }
        this.stationId = stationId;
        this.stationName = stationName;
        this.workerId = workerId;
        this.workerName = workerName;
        this.status = TaskStatus.ASSIGNED;
    }

    /**
     * 开始执行任务
     */
    public void start() {
        if (this.status != TaskStatus.ASSIGNED && this.status != TaskStatus.PAUSED) {
            throw new IllegalStateException("只有已分配或暂停的任务才能开始执行");
        }
        this.status = TaskStatus.IN_PROGRESS;
        if (this.actualStartTime == null) {
            this.actualStartTime = LocalDateTime.now();
        }
    }

    /**
     * 暂停任务
     */
    public void pause() {
        if (this.status != TaskStatus.IN_PROGRESS) {
            throw new IllegalStateException("只有进行中的任务才能暂停");
        }
        this.status = TaskStatus.PAUSED;
    }

    /**
     * 报工 - 记录完成数量
     */
    public void reportWork(int qualifiedQty, int defectiveQty) {
        if (this.status != TaskStatus.IN_PROGRESS) {
            throw new IllegalStateException("只有进行中的任务才能报工");
        }
        this.completedQty += qualifiedQty;
        this.defectiveQty += defectiveQty;

        // 自动完成判断
        if (this.completedQty + this.defectiveQty >= this.planQty) {
            this.complete();
        }
    }

    /**
     * 完成任务
     */
    public void complete() {
        this.status = TaskStatus.COMPLETED;
        this.actualEndTime = LocalDateTime.now();
    }

    /**
     * 取消任务
     */
    public void cancel() {
        if (this.status == TaskStatus.COMPLETED) {
            throw new IllegalStateException("已完成的任务不能取消");
        }
        this.status = TaskStatus.CANCELLED;
    }

    /**
     * 获取完成百分比
     */
    public int getCompletionPercentage() {
        if (planQty == null || planQty == 0) return 0;
        return (int) ((completedQty * 100.0) / planQty);
    }

    /**
     * 是否已超期
     */
    public boolean isOverdue() {
        if (planEndTime == null) return false;
        return LocalDateTime.now().isAfter(planEndTime)
                && status != TaskStatus.COMPLETED
                && status != TaskStatus.CANCELLED;
    }
}