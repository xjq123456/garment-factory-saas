package com.garment.production.interfaces.rest;

import com.garment.common.interfaces.PageResult;
import com.garment.common.interfaces.R;
import com.garment.production.application.order.ProductionOrderAppService;
import com.garment.production.application.order.dto.CreateProductionOrderCommand;
import com.garment.production.application.order.dto.ProductionOrderVO;
import com.garment.production.application.order.dto.UpdateProductionOrderCommand;
import com.garment.production.domain.order.vo.OrderStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 生产工单控制器
 */
@RestController
@RequestMapping("/api/production/orders")
@RequiredArgsConstructor
public class ProductionOrderController {

    private final ProductionOrderAppService orderAppService;

    /**
     * 创建生产工单
     */
    @PostMapping
    public R<ProductionOrderVO> createOrder(@Valid @RequestBody CreateProductionOrderCommand cmd) {
        return R.ok(orderAppService.createOrder(cmd));
    }

    /**
     * 更新生产工单
     */
    @PutMapping("/{orderId}")
    public R<ProductionOrderVO> updateOrder(@PathVariable Long orderId,
                                             @RequestBody UpdateProductionOrderCommand cmd) {
        return R.ok(orderAppService.updateOrder(orderId, cmd));
    }

    /**
     * 查询工单详情
     */
    @GetMapping("/{orderId}")
    public R<ProductionOrderVO> getOrder(@PathVariable Long orderId) {
        return R.ok(orderAppService.getOrder(orderId));
    }

    /**
     * 分页查询工单列表
     */
    @GetMapping
    public R<PageResult<ProductionOrderVO>> listOrders(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) Integer priority,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return R.ok(orderAppService.findOrders(keyword, status, priority, page, size));
    }

    /**
     * 查询逾期工单
     */
    @GetMapping("/overdue")
    public R<List<ProductionOrderVO>> getOverdueOrders() {
        return R.ok(orderAppService.findOverdueOrders());
    }

    /**
     * 审批工单
     */
    @PostMapping("/{orderId}/approve")
    public R<Void> approveOrder(@PathVariable Long orderId) {
        orderAppService.approveOrder(orderId);
        return R.ok();
    }

    /**
     * 开始生产
     */
    @PostMapping("/{orderId}/start")
    public R<Void> startProduction(@PathVariable Long orderId,
                                    @RequestParam(required = false) Long routeId) {
        orderAppService.startProduction(orderId, routeId);
        return R.ok();
    }

    /**
     * 暂停工单
     */
    @PostMapping("/{orderId}/suspend")
    public R<Void> suspendOrder(@PathVariable Long orderId) {
        orderAppService.suspendOrder(orderId);
        return R.ok();
    }

    /**
     * 恢复工单
     */
    @PostMapping("/{orderId}/resume")
    public R<Void> resumeOrder(@PathVariable Long orderId) {
        orderAppService.resumeOrder(orderId);
        return R.ok();
    }

    /**
     * 关闭工单
     */
    @PostMapping("/{orderId}/close")
    public R<Void> closeOrder(@PathVariable Long orderId) {
        orderAppService.closeOrder(orderId);
        return R.ok();
    }
}