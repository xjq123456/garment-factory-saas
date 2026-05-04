package com.garment.production.infrastructure.persistence.process;

import com.garment.production.domain.process.entity.ProcessRoute;
import org.springframework.stereotype.Component;

/**
 * 工艺路线 DO ↔ Entity 转换器
 */
@Component
public class ProcessRouteConverter {

    public ProcessRouteDO toDO(ProcessRoute entity) {
        if (entity == null) return null;
        ProcessRouteDO DO = new ProcessRouteDO();
        DO.setId(entity.getId());
        DO.setTenantId(entity.getTenantId());
        DO.setRouteCode(entity.getRouteCode());
        DO.setRouteName(entity.getRouteName());
        DO.setStyleId(entity.getStyleId());
        DO.setDescription(entity.getDescription());
        DO.setStatus(entity.getStatus());
        DO.setCreateBy(entity.getCreateBy());
        DO.setCreateTime(entity.getCreateTime());
        DO.setUpdateBy(entity.getUpdateBy());
        DO.setUpdateTime(entity.getUpdateTime());
        DO.setDeleted(entity.getDeleted());
        DO.setVersion(entity.getVersion());
        return DO;
    }

    public ProcessRoute toEntity(ProcessRouteDO DO) {
        if (DO == null) return null;
        ProcessRoute entity = new ProcessRoute();
        entity.setId(DO.getId());
        entity.setTenantId(DO.getTenantId());
        entity.setRouteCode(DO.getRouteCode());
        entity.setRouteName(DO.getRouteName());
        entity.setStyleId(DO.getStyleId());
        entity.setDescription(DO.getDescription());
        entity.setStatus(DO.getStatus());
        entity.setCreateBy(DO.getCreateBy());
        entity.setCreateTime(DO.getCreateTime());
        entity.setUpdateBy(DO.getUpdateBy());
        entity.setUpdateTime(DO.getUpdateTime());
        entity.setDeleted(DO.getDeleted());
        entity.setVersion(DO.getVersion());
        return entity;
    }
}