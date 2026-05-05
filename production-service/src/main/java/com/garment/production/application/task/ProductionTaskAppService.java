package com.garment.production.application.task;

import com.garment.common.domain.BizException;
import com.garment.common.interfaces.PageResult;
import com.garment.production.application.task.dto.*;
import com.garment.production.domain.order.entity.ProductionOrder;
import com.garment.production.domain.order.repository.ProductionOrderRepository;
import com.garment.production.domain.report.entity.ProductionReport;
import com.garment.production.domain.report.repository.ProductionReportRepository;
import com.garment.production.domain.task.entity.ProductionTask;
import com.garment.production.domain.task.event.TaskCompletedEvent;
import com.garment.production.domain.task.repository.ProductionTaskRepository;
import com.garment.production.domain.task.vo.TaskStatus;
import com.garment.production.domain.workstation.entity.Workstation;
import com.garment.production.domain.workstation.repository.WorkstationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 生产任务应用服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductionTaskAppService {

    private final ProductionTaskRepository taskRepository;
    private final ProductionOrderRepository orderRepository;
    private final WorkstationRepository workstationRepository;
    private final ProductionReportRepository reportRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 创建生产任务
     */
    @Transactional
    public ProductionTaskVO createTask(CreateTaskCommand cmd) {
        ProductionOrder order = orderRepository.findById(cmd.getOrderId())
                .orElseThrow(() -> new BizException("工单不存在: " + cmd.getOrderId()));

        String taskNo = generateTaskNo();
        ProductionTask task = ProductionTask.create(
                taskNo, cmd.getOrderId(), cmd.getStepId(), cmd.getStepName(), cmd.getPlanQty()
        );
        task.setRouteId(cmd.getRouteId());
        task.setPlanStartTime(cmd.getPlanStartTime());
        task.setPlanEndTime(cmd.getPlanEndTime());
        task.setPriority(cmd.getPriority() != null ? cmd.getPriority() : 0);
        task.setRemark(cmd.getRemark());

        taskRepository.save(task);

        log.info("生产任务创建成功: taskNo={}, stepName={}, planQty={}",
                taskNo, cmd.getStepName(), cmd.getPlanQty());

        return toVO(task);
    }

    /**
     * 分配任务到工位和工人
     */
    @Transactional
    public void assignTask(AssignTaskCommand cmd) {
        ProductionTask task = getTaskOrThrow(cmd.getTaskId());

        Workstation station = workstationRepository.findById(cmd.getStationId())
                .orElseThrow(() -> new BizException("工位不存在: " + cmd.getStationId()));

        if (!station.isAvailable()) {
            throw new BizException("工位不可用: " + station.getStationCode());
        }

        // 分配任务
        task.assign(cmd.getStationId(), station.getStationName(),
                cmd.getWorkerId(), cmd.getWorkerName());

        // 工位标记为忙碌
        station.markBusy();
        if (cmd.getWorkerId() != null) {
            station.bindWorker(cmd.getWorkerId(), cmd.getWorkerName());
        }

        taskRepository.update(task);
        workstationRepository.update(station);

        log.info("任务分配成功: taskNo={}, station={}",
                task.getTaskNo(), station.getStationCode());
    }

    /**
     * 开始执行任务
     */
    @Transactional
    public void startTask(Long taskId) {
        ProductionTask task = getTaskOrThrow(taskId);
        task.start();
        taskRepository.update(task);
        log.info("任务开始执行: taskNo={}", task.getTaskNo());
    }

    /**
     * 暂停任务
     */
    @Transactional
    public void pauseTask(Long taskId) {
        ProductionTask task = getTaskOrThrow(taskId);
        task.pause();
        taskRepository.update(task);
        log.info("任务暂停: taskNo={}", task.getTaskNo());
    }

    /**
     * 报工 - 核心业务操作
     * 1. 更新任务完成数量
     * 2. 创建报工记录
     * 3. 同步更新工单完成数量
     */
    @Transactional
    public ProductionTaskVO reportWork(ReportWorkCommand cmd) {
        ProductionTask task = getTaskOrThrow(cmd.getTaskId());

        // 校验报工数据
        if (cmd.getReportQty() != cmd.getQualifiedQty() + cmd.getDefectiveQty()) {
            throw new BizException("报工数量必须等于合格数量与不良品数量之和");
        }

        // 更新任务
        task.reportWork(cmd.getQualifiedQty(), cmd.getDefectiveQty());
        if (cmd.getStationId() != null) {
            task.setStationId(cmd.getStationId());
        }
        taskRepository.update(task);

        // 创建报工记录
        ProductionReport report = ProductionReport.create(
                generateReportNo(), task.getOrderId(), task.getId(),
                task.getWorkerId(), task.getWorkerName()
        );
        report.setStationId(cmd.getStationId() != null ? cmd.getStationId() : task.getStationId());
        report.submit(cmd.getReportQty(), cmd.getQualifiedQty(), cmd.getDefectiveQty());
        report.setWorkHours(cmd.getWorkHours());
        report.setRemark(cmd.getRemark());
        reportRepository.save(report);

        // 同步更新工单数量
        ProductionOrder order = orderRepository.findById(task.getOrderId())
                .orElseThrow(() -> new BizException("工单不存在: " + task.getOrderId()));
        order.addProduction(cmd.getQualifiedQty(), cmd.getDefectiveQty());
        orderRepository.update(order);

        // 如果任务完成，发布事件
        if (task.getStatus() == TaskStatus.COMPLETED) {
            // 释放工位
            if (task.getStationId() != null) {
                workstationRepository.findById(task.getStationId()).ifPresent(station -> {
                    station.markIdle();
                    workstationRepository.update(station);
                });
            }

            eventPublisher.publishEvent(new TaskCompletedEvent(
                    task.getId(), task.getTaskNo(), task.getOrderId(),
                    task.getStepId(), task.getCompletedQty(), task.getDefectiveQty()
            ));
        }

        log.info("报工成功: taskNo={}, reportQty={}, qualified={}, defective={}",
                task.getTaskNo(), cmd.getReportQty(), cmd.getQualifiedQty(), cmd.getDefectiveQty());

        return toVO(task);
    }

    /**
     * 取消任务
     */
    @Transactional
    public void cancelTask(Long taskId) {
        ProductionTask task = getTaskOrThrow(taskId);
        task.cancel();
        taskRepository.update(task);

        // 释放工位
        if (task.getStationId() != null) {
            workstationRepository.findById(task.getStationId()).ifPresent(station -> {
                station.markIdle();
                workstationRepository.update(station);
            });
        }

        log.info("任务已取消: taskNo={}", task.getTaskNo());
    }

    /**
     * 查询任务详情
     */
    public ProductionTaskVO getTask(Long taskId) {
        return toVO(getTaskOrThrow(taskId));
    }

    /**
     * 按工单查询任务列表
     */
    public List<ProductionTaskVO> listTasksByOrder(Long orderId) {
        return taskRepository.findByOrderId(orderId).stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    /**
     * 按工人查询任务列表
     */
    public List<ProductionTaskVO> listTasksByWorker(Long workerId, TaskStatus status) {
        return taskRepository.findByWorkerId(workerId, status).stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    /**
     * 分页查询任务
     */
    public PageResult<ProductionTaskVO> findTasks(Long orderId, Long workerId,
                                                   TaskStatus status, int page, int size) {
        PageResult<ProductionTask> pageResult = taskRepository.findPage(
                orderId, workerId, status, page, size);

        List<ProductionTaskVO> voList = pageResult.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        return new PageResult<>(voList, pageResult.getTotal(), page, size);
    }

    /**
     * 查询逾期任务
     */
    public List<ProductionTaskVO> findOverdueTasks() {
        return taskRepository.findOverdueTasks().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    // ============ private methods ============

    private ProductionTask getTaskOrThrow(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new BizException("生产任务不存在: " + taskId));
    }

    private String generateTaskNo() {
        return "PT" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    private String generateReportNo() {
        return "RPT" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    private ProductionTaskVO toVO(ProductionTask task) {
        ProductionTaskVO vo = new ProductionTaskVO();
        vo.setId(task.getId());
        vo.setTaskNo(task.getTaskNo());
        vo.setOrderId(task.getOrderId());
        vo.setRouteId(task.getRouteId());
        vo.setStepId(task.getStepId());
        vo.setStepName(task.getStepName());
        vo.setStationId(task.getStationId());
        vo.setStationName(task.getStationName());
        vo.setWorkerId(task.getWorkerId());
        vo.setWorkerName(task.getWorkerName());
        vo.setPlanQty(task.getPlanQty());
        vo.setCompletedQty(task.getCompletedQty());
        vo.setDefectiveQty(task.getDefectiveQty());
        vo.setUnit(task.getUnit());
        vo.setPlanStartTime(task.getPlanStartTime());
        vo.setPlanEndTime(task.getPlanEndTime());
        vo.setActualStartTime(task.getActualStartTime());
        vo.setActualEndTime(task.getActualEndTime());
        vo.setStatus(task.getStatus());
        vo.setPriority(task.getPriority());
        vo.setCompletionPercentage(task.getCompletionPercentage());
        vo.setOverdue(task.isOverdue());
        vo.setRemark(task.getRemark());
        vo.setCreateTime(task.getCreateTime());
        vo.setUpdateTime(task.getUpdateTime());
        return vo;
    }
}