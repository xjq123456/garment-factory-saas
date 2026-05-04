package com.garment.production.domain.report.repository;

import com.garment.production.domain.report.entity.ProductionReport;

import java.util.List;
import java.util.Optional;

/**
 * 报工记录仓储接口
 */
public interface ProductionReportRepository {

    void save(ProductionReport report);

    Optional<ProductionReport> findById(Long id);

    Optional<ProductionReport> findByReportNo(String reportNo);

    /**
     * 按工单查询报工记录
     */
    List<ProductionReport> findByOrderId(Long orderId);

    /**
     * 按任务查询报工记录
     */
    List<ProductionReport> findByTaskId(Long taskId);

    /**
     * 按工人查询报工记录
     */
    List<ProductionReport> findByWorkerId(Long workerId);
}