package com.garment.production.domain.quality.repository;

import com.garment.common.interfaces.PageResult;
import com.garment.production.domain.quality.entity.QualityInspection;
import com.garment.production.domain.quality.vo.InspectionResult;
import com.garment.production.domain.quality.vo.InspectionType;

import java.util.List;
import java.util.Optional;

/**
 * 质检记录仓储接口
 */
public interface QualityInspectionRepository {

    void save(QualityInspection inspection);

    void update(QualityInspection inspection);

    Optional<QualityInspection> findById(Long id);

    Optional<QualityInspection> findByInspectionNo(String inspectionNo);

    /**
     * 按工单查询质检记录
     */
    List<QualityInspection> findByOrderId(Long orderId);

    /**
     * 按任务查询质检记录
     */
    List<QualityInspection> findByTaskId(Long taskId);

    /**
     * 分页查询
     */
    PageResult<QualityInspection> findPage(Long tenantId, Long orderId, InspectionType type,
                                            InspectionResult result, int page, int size);

    /**
     * 统计不合格数量
     */
    long countReject(Long tenantId, Long orderId);
}