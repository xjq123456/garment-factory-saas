package com.garment.production.domain.workstation.entity;

import com.garment.common.domain.AggregateRoot;
import com.garment.production.domain.workstation.vo.WorkstationStatus;
import com.garment.production.domain.workstation.vo.WorkstationType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 工位 - 聚合根
 *
 * 代表生产车间中的一个工作位置，可绑定工人和设备。
 * 工位类型决定它能执行哪些工序。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Workstation extends AggregateRoot {

    /** 工位编号 */
    private String stationCode;

    /** 工位名称 */
    private String stationName;

    /** 车间 */
    private String workshop;

    /** 产线 */
    private String productionLine;

    /** 工位类型 */
    private WorkstationType stationType;

    /** 当前绑定工人ID */
    private Long workerId;

    /** 当前绑定工人姓名 */
    private String workerName;

    /** 设备编号 */
    private String equipmentCode;

    /** 工位状态 */
    private WorkstationStatus status;

    /** 备注 */
    private String remark;

    /**
     * 创建工位
     */
    public static Workstation create(String stationCode, String stationName,
                                     String workshop, WorkstationType stationType) {
        Workstation station = new Workstation();
        station.setStationCode(stationCode);
        station.setStationName(stationName);
        station.setWorkshop(workshop);
        station.setStationType(stationType);
        station.setStatus(WorkstationStatus.IDLE);
        return station;
    }

    /**
     * 绑定工人
     */
    public void bindWorker(Long workerId, String workerName) {
        this.workerId = workerId;
        this.workerName = workerName;
    }

    /**
     * 解绑工人
     */
    public void unbindWorker() {
        this.workerId = null;
        this.workerName = null;
    }

    /**
     * 标记为忙碌
     */
    public void markBusy() {
        if (this.status == WorkstationStatus.DISABLED) {
            throw new IllegalStateException("已停用的工位不能分配任务");
        }
        if (this.status == WorkstationStatus.MAINTENANCE) {
            throw new IllegalStateException("维护中的工位不能分配任务");
        }
        this.status = WorkstationStatus.BUSY;
    }

    /**
     * 标记为空闲
     */
    public void markIdle() {
        this.status = WorkstationStatus.IDLE;
    }

    /**
     * 标记为维护中
     */
    public void markMaintenance() {
        this.status = WorkstationStatus.MAINTENANCE;
    }

    /**
     * 停用工位
     */
    public void disable() {
        this.status = WorkstationStatus.DISABLED;
    }

    /**
     * 是否可用（可以分配任务）
     */
    public boolean isAvailable() {
        return this.status == WorkstationStatus.IDLE;
    }
}