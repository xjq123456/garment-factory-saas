package com.garment.order.interfaces.rest;

import com.garment.common.interfaces.R;
import com.garment.common.interfaces.PageResult;
import com.garment.order.application.order.ProductionOrderAppService;
import com.garment.order.application.order.dto.CreateProductionOrderCommand;
import com.garment.order.domain.order.entity.ProductionOrder;
import com.garment.order.domain.order.vo.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/production-orders")
@RequiredArgsConstructor
public class ProductionOrderController {

    private final ProductionOrderAppService orderAppService;

    @PostMapping
    public R<ProductionOrder> create(@RequestBody CreateProductionOrderCommand cmd) {
        return R.ok(orderAppService.create(cmd));
    }

    @GetMapping("/{id}")
    public R<ProductionOrder> getById(@PathVariable Long id) {
        return R.ok(orderAppService.findById(id));
    }

    @GetMapping
    public R<PageResult<ProductionOrder>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) OrderStatus status) {
        return R.ok(orderAppService.findPage(pageNum, pageSize, keyword, status));
    }

    @PutMapping("/{id}/confirm")
    public R<Void> confirm(@PathVariable Long id) {
        orderAppService.confirm(id);
        return R.ok();
    }

    @PutMapping("/{id}/start-production")
    public R<Void> startProduction(@PathVariable Long id) {
        orderAppService.startProduction(id);
        return R.ok();
    }

    @PutMapping("/{id}/complete")
    public R<Void> complete(@PathVariable Long id) {
        orderAppService.complete(id);
        return R.ok();
    }

    @PutMapping("/{id}/cancel")
    public R<Void> cancel(@PathVariable Long id) {
        orderAppService.cancel(id);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        orderAppService.delete(id);
        return R.ok();
    }
}