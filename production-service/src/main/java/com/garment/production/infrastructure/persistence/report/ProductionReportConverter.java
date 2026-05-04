package com.garment.production.infrastructure.persistence.report;

import com.garment.production.domain.report.entity.ProductionReport;
import org.springframework.stereotype.Component;

/**
 * 报工记录 DO ↔ Entity 转换器
 */
@Component
public class ProductionReportConverter {

    public ProductionReportDO toDO(ProductionReport entity) {
        if (entity == null) return null;
        ProductionReportDO DO = new ProductionReportDO();
        DO.setId(entity.getId());
        DO.setTenantId(entity.getTenantId());
        DO.setReportNo(entity.getReportNo());
        DO.setOrderId(entity.getOrderId());
        DO.setTaskId(entity.getTaskId());
        DO.setStationId(entity.getStationId());
        DO.setWorkerId(entity.getWorkerId());
        DO.setWorkerName(entity.getWorkerName());
        DO.setReportQty(entity.getReportQty());
        DO.setQualifiedQty(entity.getQualifiedQty());
        DO.setDefectiveQty(entity.getDefectiveQty());
        DO.setWorkHours(entity.getWorkHours());
        DO.setReportTime(entity.getReportTime());
        DO.setRemark(entity.getRemark());
        DO.setCreateBy(entity.getCreateBy());
        DO.setCreateTime(entity.getCreateTime());
        DO.setUpdateBy(entity.getUpdateBy());
        DO.setUpdateTime(entity.getUpdateTime());
        DO.setDeleted(entity.getDeleted());
        return DO;
    }

    public ProductionReport toEntity(ProductionReportDO DO) {
        if (DO == null) return null;
        ProductionReport entity = new ProductionReport();
        entity.setId(DO.getId());
        entity.setTenantId(DO.getTenantId());
        entity.setReportNo(DO.getReportNo());
        entity.setOrderId(DO.getOrderId());
        entity.setTaskId(DO.getTaskId());
        entity.setStationId(DO.getStationId());
        entity.setWorkerId(DO.getWorkerId());
        entity.setWorkerName(DO.getWorkerName());
        entity.setReportQty(DO.getReportQty());
        entity.setQualifiedQty(DO.getQualifiedQty());
        entity.setDefectiveQty(DO.getDefectiveQty());
        entity.setWorkHours(DO.getWorkHours());
        entity.setReportTime(DO.getReportTime());
        entity.setRemark(DO.getRemark());
        entity.setCreateBy(DO.getCreateBy());
        entity.setCreateTime(DO.getCreateTime());
        entity.setUpdateBy(DO.getUpdateBy());
        entity.setUpdateTime(DO.getUpdateTime());
        entity.setDeleted(DO.getDeleted());
        return entity;
    }
}