package com.garment.production.infrastructure.persistence.process;

import com.garment.production.domain.process.entity.ProcessStep;
import com.garment.production.domain.workstation.vo.WorkstationType;
import org.springframework.stereotype.Component;

/**
 * 工序步骤 DO ↔ Entity 转换器
 */
@Component
public class ProcessStepConverter {

    public ProcessStepDO toDO(ProcessStep entity) {
        if (entity == null) return null;
        ProcessStepDO DO = new ProcessStepDO();
        DO.setId(entity.getId());
        DO.setTenantId(entity.getTenantId());
        DO.setRouteId(entity.getRouteId());
        DO.setStepNo(entity.getStepNo());
        DO.setStepName(entity.getStepName());
        DO.setStepType(entity.getStepType() != null ? entity.getStepType().getCode() : null);
        DO.setStandardTime(entity.getStandardTime());
        DO.setStandardOutput(entity.getStandardOutput());
        DO.setDescription(entity.getDescription());
        DO.setCreateBy(entity.getCreateBy());
        DO.setCreateTime(entity.getCreateTime());
        DO.setUpdateBy(entity.getUpdateBy());
        DO.setUpdateTime(entity.getUpdateTime());
        return DO;
    }

    public ProcessStep toEntity(ProcessStepDO DO) {
        if (DO == null) return null;
        ProcessStep entity = new ProcessStep();
        entity.setId(DO.getId());
        entity.setTenantId(DO.getTenantId());
        entity.setRouteId(DO.getRouteId());
        entity.setStepNo(DO.getStepNo());
        entity.setStepName(DO.getStepName());
        entity.setStepType(DO.getStepType() != null ? WorkstationType.valueOf(DO.getStepType()) : null);
        entity.setStandardTime(DO.getStandardTime());
        entity.setStandardOutput(DO.getStandardOutput());
        entity.setDescription(DO.getDescription());
        entity.setCreateBy(DO.getCreateBy());
        entity.setCreateTime(DO.getCreateTime());
        entity.setUpdateBy(DO.getUpdateBy());
        entity.setUpdateTime(DO.getUpdateTime());
        return entity;
    }
}