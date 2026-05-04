package com.garment.production.interfaces.rest;

import com.garment.common.interfaces.PageResult;
import com.garment.common.interfaces.R;
import com.garment.production.application.task.ProductionTaskAppService;
import com.garment.production.application.task.dto.*;
import com.garment.production.domain.task.vo.TaskStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 生产任务控制器
 */
@RestController
@RequestMapping("/api/production/tasks")
@RequiredArgsConstructor
public class ProductionTaskController {

    private final ProductionTaskAppService taskAppService;

    /**
     * 创建生产任务
     */
    @PostMapping
    public R<ProductionTaskVO> createTask(@Valid @RequestBody CreateTaskCommand cmd) {
        return R.ok(taskAppService.createTask(cmd));
    }

    /**
     * 查询任务详情
     */
    @GetMapping("/{taskId}")
    public R<ProductionTaskVO> getTask(@PathVariable Long taskId) {
        return R.ok(taskAppService.getTask(taskId));
    }

    /**
     * 按工单查询任务列表
     */
    @GetMapping("/order/{orderId}")
    public R<List<ProductionTaskVO>> listByOrder(@PathVariable Long orderId) {
        return R.ok(taskAppService.listTasksByOrder(orderId));
    }

    /**
     * 按工人查询任务列表
     */
    @GetMapping("/worker/{workerId}")
    public R<List<ProductionTaskVO>> listByWorker(
            @PathVariable Long workerId,
            @RequestParam(required = false) TaskStatus status) {
        return R.ok(taskAppService.listTasksByWorker(workerId, status));
    }

    /**
     * 分页查询任务
     */
    @GetMapping
    public R<PageResult<ProductionTaskVO>> listTasks(
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) Long workerId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return R.ok(taskAppService.findTasks(orderId, workerId, status, page, size));
    }

    /**
     * 查询逾期任务
     */
    @GetMapping("/overdue")
    public R<List<ProductionTaskVO>> getOverdueTasks() {
        return R.ok(taskAppService.findOverdueTasks());
    }

    /**
     * 分配任务
     */
    @PostMapping("/assign")
    public R<Void> assignTask(@Valid @RequestBody AssignTaskCommand cmd) {
        taskAppService.assignTask(cmd);
        return R.ok();
    }

    /**
     * 开始执行任务
     */
    @PostMapping("/{taskId}/start")
    public R<Void> startTask(@PathVariable Long taskId) {
        taskAppService.startTask(taskId);
        return R.ok();
    }

    /**
     * 暂停任务
     */
    @PostMapping("/{taskId}/pause")
    public R<Void> pauseTask(@PathVariable Long taskId) {
        taskAppService.pauseTask(taskId);
        return R.ok();
    }

    /**
     * 报工
     */
    @PostMapping("/report-work")
    public R<ProductionTaskVO> reportWork(@Valid @RequestBody ReportWorkCommand cmd) {
        return R.ok(taskAppService.reportWork(cmd));
    }

    /**
     * 取消任务
     */
    @PostMapping("/{taskId}/cancel")
    public R<Void> cancelTask(@PathVariable Long taskId) {
        taskAppService.cancelTask(taskId);
        return R.ok();
    }
}