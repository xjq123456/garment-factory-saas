package com.garment.production.interfaces.rest;

import com.garment.common.interfaces.R;
import com.garment.production.application.workstation.WorkstationAppService;
import com.garment.production.application.workstation.dto.CreateWorkstationCommand;
import com.garment.production.application.workstation.dto.WorkstationVO;
import com.garment.production.domain.workstation.vo.WorkstationType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工位管理控制器
 */
@RestController
@RequestMapping("/api/production/workstations")
@RequiredArgsConstructor
public class WorkstationController {

    private final WorkstationAppService workstationAppService;

    /**
     * 创建工位
     */
    @PostMapping
    public R<WorkstationVO> createWorkstation(@Valid @RequestBody CreateWorkstationCommand cmd) {
        return R.ok(workstationAppService.createWorkstation(cmd));
    }

    /**
     * 查询工位详情
     */
    @GetMapping("/{stationId}")
    public R<WorkstationVO> getStation(@PathVariable Long stationId) {
        return R.ok(workstationAppService.getStation(stationId));
    }

    /**
     * 查询所有工位
     */
    @GetMapping
    public R<List<WorkstationVO>> listAllStations() {
        return R.ok(workstationAppService.listAllStations());
    }

    /**
     * 查询可用工位（按类型筛选）
     */
    @GetMapping("/available")
    public R<List<WorkstationVO>> listAvailableStations(
            @RequestParam(required = false) WorkstationType type) {
        return R.ok(workstationAppService.listAvailableStations(type));
    }

    /**
     * 按车间查询工位
     */
    @GetMapping("/workshop/{workshop}")
    public R<List<WorkstationVO>> listByWorkshop(@PathVariable String workshop) {
        return R.ok(workstationAppService.listStationsByWorkshop(workshop));
    }

    /**
     * 绑定工人
     */
    @PostMapping("/{stationId}/bind-worker")
    public R<Void> bindWorker(@PathVariable Long stationId,
                               @RequestParam Long workerId,
                               @RequestParam String workerName) {
        workstationAppService.bindWorker(stationId, workerId, workerName);
        return R.ok();
    }

    /**
     * 解绑工人
     */
    @PostMapping("/{stationId}/unbind-worker")
    public R<Void> unbindWorker(@PathVariable Long stationId) {
        workstationAppService.unbindWorker(stationId);
        return R.ok();
    }

    /**
     * 工位进入维护
     */
    @PostMapping("/{stationId}/maintenance")
    public R<Void> markMaintenance(@PathVariable Long stationId) {
        workstationAppService.markMaintenance(stationId);
        return R.ok();
    }

    /**
     * 工位恢复空闲
     */
    @PostMapping("/{stationId}/idle")
    public R<Void> markIdle(@PathVariable Long stationId) {
        workstationAppService.markIdle(stationId);
        return R.ok();
    }

    /**
     * 停用工位
     */
    @PostMapping("/{stationId}/disable")
    public R<Void> disableStation(@PathVariable Long stationId) {
        workstationAppService.disableStation(stationId);
        return R.ok();
    }
}