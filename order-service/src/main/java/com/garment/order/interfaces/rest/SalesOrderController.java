package com.garment.order.interfaces.rest;

import com.garment.common.interfaces.R;
import com.garment.common.interfaces.PageResult;
import com.garment.order.application.sales.SalesOrderAppService;
import com.garment.order.application.sales.dto.CreateSalesOrderCommand;
import com.garment.order.domain.sales.entity.SalesOrder;
import com.garment.order.domain.sales.vo.SalesOrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderAppService salesAppService;

    @PostMapping
    public R<SalesOrder> create(@RequestBody CreateSalesOrderCommand cmd) {
        return R.ok(salesAppService.create(cmd));
    }

    @GetMapping("/{id}")
    public R<SalesOrder> getById(@PathVariable Long id) {
        return R.ok(salesAppService.findById(id));
    }

    @GetMapping
    public R<PageResult<SalesOrder>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) SalesOrderStatus status) {
        return R.ok(salesAppService.findPage(pageNum, pageSize, keyword, status));
    }

    @PutMapping("/{id}/confirm")
    public R<Void> confirm(@PathVariable Long id) {
        salesAppService.confirm(id);
        return R.ok();
    }

    @PutMapping("/{id}/ship")
    public R<Void> ship(@PathVariable Long id) {
        salesAppService.ship(id);
        return R.ok();
    }

    @PutMapping("/{id}/complete")
    public R<Void> complete(@PathVariable Long id) {
        salesAppService.complete(id);
        return R.ok();
    }

    @PutMapping("/{id}/cancel")
    public R<Void> cancel(@PathVariable Long id) {
        salesAppService.cancel(id);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        salesAppService.delete(id);
        return R.ok();
    }
}
