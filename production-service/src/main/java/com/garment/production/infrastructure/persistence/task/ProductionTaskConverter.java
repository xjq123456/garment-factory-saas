package com.garment.production.infrastructure.persistence.task;

import com.garment.production.domain.task.entity.ProductionTask;
import com.garment.production.domain.task.vo.TaskStatus;
import org.springframework.stereotype.Component;

/**
 * 生产任务 DO ↔ Entity 转换器
 */
@Component
public class ProductionTaskConverter {

    public ProductionTaskDO toDO(ProductionTask entity) {
        if (entity == null) return null;
        ProductionTaskDO DO = new ProductionTaskDO();
        DO.setId(entity.getId());
        DO.setTenantId(entity.getTenantId());
        DO.setTaskNo(entity.getTaskNo());
        DO.setOrderId(entity.getOrderId());
        DO.setRouteId(entity.getRouteId());
        DO.setStepId(entity.getStepId());
        DO.setStepName(entity.getStepName());
        DO.setStationId(entity.getStationId());
        DO.setStationName(entity.getStationName());
        DO.setWorkerId(entity.getWorkerId());
        DO.setWorkerName(entity.getWorkerName());
        DO.setPlanQty(entity.getPlanQty());
        DO.setCompletedQty(entity.getCompletedQty());
        DO.setDefectiveQty(entity.getDefectiveQty());
        DO.setUnit(entity.getUnit());
        DO.setPlanStartTime(entity.getPlanStartTime());
        DO.setPlanEndTime(entity.getPlanEndTime());
        DO.setActualStartTime(entity.getActualStartTime());
        DO.setActualEndTime(entity.getActualEndTime());
        DO.setStatus(entity.getStatus() != null ? entity.getStatus().getCode() : null);
        DO.setPriority(entity.getPriority());
        DO.setRemark(entity.getRemark());
        DO.setCreateBy(entity.getCreateBy());
        DO.setCreateTime(entity.getCreateTime());
        DO.setUpdateBy(entity.getUpdateBy());
        DO.setUpdateTime(entity.getUpdateTime());
        DO.setDeleted(entity.getDeleted());
        DO.setVersion(entity.getVersion());
        return DO;
    }

    public ProductionTask toEntity(ProductionTaskDO DO) {
        if (DO == null) return null;
        ProductionTask entity = new ProductionTask();
        entity.setId(DO.getId());
        entity.setTenantId(DO.getTenantId());
        entity.setTaskNo(DO.getTaskNo());
        entity.setOrderId(DO.getOrderId());
        entity.setRouteId(DO.getRouteId());
        entity.setStepId(DO.getStepId());
        entity.setStepName(DO.getStepName());
        entity.setStationId(DO.getStationId());
        entity.setStationName(DO.getStationName());
        entity.setWorkerId(DO.getWorkerId());
        entity.setWorkerName(DO.getWorkerName());
        entity.setPlanQty(DO.getPlanQty());
        entity.setCompletedQty(DO.getCompletedQty());
        entity.setDefectiveQty(DO.getDefectiveQty());
        entity.setUnit(DO.getUnit());
        entity.setPlanStartTime(DO.getPlanStartTime());
        entity.setPlanEndTime(DO.getPlanEndTime());
        entity.setActualStartTime(DO.getActualStartTime());
        entity.setActualEndTime(DO.getActualEndTime());
        entity.setStatus(DO.getStatus() != null ? TaskStatus.valueOf(DO.getStatus()) : null);
        entity.setPriority(DO.getPriority());
        entity.setRemark(DO.getRemark());
        entity.setCreateBy(DO.getCreateBy());
        entity.setCreateTime(DO.getCreateTime());
        entity.setUpdateBy(DO.getUpdateBy());
        entity.setUpdateTime(DO.getUpdateTime());
        entity.setDeleted(DO.getDeleted());
        entity.setVersion(DO.getVersion());
        return entity;
    }
}