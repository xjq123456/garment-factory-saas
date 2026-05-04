package com.garment.production.infrastructure.persistence.workstation;

import com.garment.production.domain.workstation.entity.Workstation;
import com.garment.production.domain.workstation.vo.WorkstationStatus;
import com.garment.production.domain.workstation.vo.WorkstationType;
import org.springframework.stereotype.Component;

/**
 * 工位 DO ↔ Entity 转换器
 */
@Component
public class WorkstationConverter {

    public WorkstationDO toDO(Workstation entity) {
        if (entity == null) return null;
        WorkstationDO DO = new WorkstationDO();
        DO.setId(entity.getId());
        DO.setTenantId(entity.getTenantId());
        DO.setStationCode(entity.getStationCode());
        DO.setStationName(entity.getStationName());
        DO.setWorkshop(entity.getWorkshop());
        DO.setProductionLine(entity.getProductionLine());
        DO.setStationType(entity.getStationType() != null ? entity.getStationType().getCode() : null);
        DO.setWorkerId(entity.getWorkerId());
        DO.setWorkerName(entity.getWorkerName());
        DO.setEquipmentCode(entity.getEquipmentCode());
        DO.setStatus(entity.getStatus() != null ? entity.getStatus().getCode() : null);
        DO.setRemark(entity.getRemark());
        DO.setCreateBy(entity.getCreateBy());
        DO.setCreateTime(entity.getCreateTime());
        DO.setUpdateBy(entity.getUpdateBy());
        DO.setUpdateTime(entity.getUpdateTime());
        DO.setDeleted(entity.getDeleted());
        DO.setVersion(entity.getVersion());
        return DO;
    }

    public Workstation toEntity(WorkstationDO DO) {
        if (DO == null) return null;
        Workstation entity = new Workstation();
        entity.setId(DO.getId());
        entity.setTenantId(DO.getTenantId());
        entity.setStationCode(DO.getStationCode());
        entity.setStationName(DO.getStationName());
        entity.setWorkshop(DO.getWorkshop());
        entity.setProductionLine(DO.getProductionLine());
        entity.setStationType(DO.getStationType() != null ? WorkstationType.valueOf(DO.getStationType()) : null);
        entity.setWorkerId(DO.getWorkerId());
        entity.setWorkerName(DO.getWorkerName());
        entity.setEquipmentCode(DO.getEquipmentCode());
        entity.setStatus(DO.getStatus() != null ? WorkstationStatus.valueOf(DO.getStatus()) : null);
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