package com.garment.production.domain.task.repository;

import com.garment.common.interfaces.PageResult;
import com.garment.production.domain.task.entity.ProductionTask;
import com.garment.production.domain.task.vo.TaskStatus;

import java.util.List;
import java.util.Optional;

/**
 * 生产任务仓储接口
 */
public interface ProductionTaskRepository {

    void save(ProductionTask task);

    void update(ProductionTask task);

    Optional<ProductionTask> findById(Long id);

    Optional<ProductionTask> findByTaskNo(String taskNo);

    /**
     * 按工单查询任务列表
     */
    List<ProductionTask> findByOrderId(Long orderId);

    /**
     * 按工位查询任务列表
     */
    List<ProductionTask> findByStationId(Long stationId, TaskStatus status);

    /**
     * 按工人查询任务列表
     */
    List<ProductionTask> findByWorkerId(Long workerId, TaskStatus status);

    /**
     * 分页查询
     */
    PageResult<ProductionTask> findPage(Long tenantId, Long orderId, Long workerId,
                                         TaskStatus status, int page, int size);

    /**
     * 查询逾期任务
     */
    List<ProductionTask> findOverdueTasks(Long tenantId);
}