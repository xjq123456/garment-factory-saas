package com.garment.production.infrastructure.persistence.quality;

import com.garment.production.domain.quality.entity.QualityInspection;
import com.garment.production.domain.quality.vo.InspectionResult;
import com.garment.production.domain.quality.vo.InspectionType;
import org.springframework.stereotype.Component;

/**
 * 质检记录 DO ↔ Entity 转换器
 */
@Component
public class QualityInspectionConverter {

    public QualityInspectionDO toDO(QualityInspection entity) {
        if (entity == null) return null;
        QualityInspectionDO DO = new QualityInspectionDO();
        DO.setId(entity.getId());
        DO.setTenantId(entity.getTenantId());
        DO.setInspectionNo(entity.getInspectionNo());
        DO.setOrderId(entity.getOrderId());
        DO.setTaskId(entity.getTaskId());
        DO.setInspectionType(entity.getInspectionType() != null ? entity.getInspectionType().getCode() : null);
        DO.setInspectQty(entity.getInspectQty());
        DO.setPassQty(entity.getPassQty());
        DO.setRejectQty(entity.getRejectQty());
        DO.setDefectTypes(entity.getDefectTypes());
        DO.setDefectDesc(entity.getDefectDesc());
        DO.setResult(entity.getResult() != null ? entity.getResult().getCode() : null);
        DO.setInspectorId(entity.getInspectorId());
        DO.setInspectorName(entity.getInspectorName());
        DO.setInspectionTime(entity.getInspectionTime());
        DO.setRemark(entity.getRemark());
        DO.setCreateBy(entity.getCreateBy());
        DO.setCreateTime(entity.getCreateTime());
        DO.setUpdateBy(entity.getUpdateBy());
        DO.setUpdateTime(entity.getUpdateTime());
        DO.setDeleted(entity.getDeleted());
        return DO;
    }

    public QualityInspection toEntity(QualityInspectionDO DO) {
        if (DO == null) return null;
        QualityInspection entity = new QualityInspection();
        entity.setId(DO.getId());
        entity.setTenantId(DO.getTenantId());
        entity.setInspectionNo(DO.getInspectionNo());
        entity.setOrderId(DO.getOrderId());
        entity.setTaskId(DO.getTaskId());
        entity.setInspectionType(DO.getInspectionType() != null ? InspectionType.valueOf(DO.getInspectionType()) : null);
        entity.setInspectQty(DO.getInspectQty());
        entity.setPassQty(DO.getPassQty());
        entity.setRejectQty(DO.getRejectQty());
        entity.setDefectTypes(DO.getDefectTypes());
        entity.setDefectDesc(DO.getDefectDesc());
        entity.setResult(DO.getResult() != null ? InspectionResult.valueOf(DO.getResult()) : null);
        entity.setInspectorId(DO.getInspectorId());
        entity.setInspectorName(DO.getInspectorName());
        entity.setInspectionTime(DO.getInspectionTime());
        entity.setRemark(DO.getRemark());
        entity.setCreateBy(DO.getCreateBy());
        entity.setCreateTime(DO.getCreateTime());
        entity.setUpdateBy(DO.getUpdateBy());
        entity.setUpdateTime(DO.getUpdateTime());
        entity.setDeleted(DO.getDeleted());
        return entity;
    }
}