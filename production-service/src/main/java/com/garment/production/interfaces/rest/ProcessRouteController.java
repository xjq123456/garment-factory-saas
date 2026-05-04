package com.garment.production.interfaces.rest;

import com.garment.common.interfaces.R;
import com.garment.production.application.process.ProcessRouteAppService;
import com.garment.production.application.process.dto.CreateProcessRouteCommand;
import com.garment.production.application.process.dto.ProcessRouteVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工艺路线控制器
 */
@RestController
@RequestMapping("/api/production/routes")
@RequiredArgsConstructor
public class ProcessRouteController {

    private final ProcessRouteAppService routeAppService;

    /**
     * 创建工艺路线（含工序步骤）
     */
    @PostMapping
    public R<ProcessRouteVO> createRoute(@Valid @RequestBody CreateProcessRouteCommand cmd) {
        return R.ok(routeAppService.createRoute(cmd));
    }

    /**
     * 查询工艺路线详情
     */
    @GetMapping("/{routeId}")
    public R<ProcessRouteVO> getRoute(@PathVariable Long routeId) {
        return R.ok(routeAppService.getRoute(routeId));
    }

    /**
     * 查询所有启用的工艺路线
     */
    @GetMapping("/active")
    public R<List<ProcessRouteVO>> listActiveRoutes() {
        return R.ok(routeAppService.listActiveRoutes());
    }

    /**
     * 按款式查询工艺路线
     */
    @GetMapping("/style/{styleId}")
    public R<List<ProcessRouteVO>> listRoutesByStyle(@PathVariable Long styleId) {
        return R.ok(routeAppService.listRoutesByStyle(styleId));
    }

    /**
     * 启用工艺路线
     */
    @PostMapping("/{routeId}/activate")
    public R<Void> activateRoute(@PathVariable Long routeId) {
        routeAppService.activateRoute(routeId);
        return R.ok();
    }

    /**
     * 停用工艺路线
     */
    @PostMapping("/{routeId}/deactivate")
    public R<Void> deactivateRoute(@PathVariable Long routeId) {
        routeAppService.deactivateRoute(routeId);
        return R.ok();
    }
}