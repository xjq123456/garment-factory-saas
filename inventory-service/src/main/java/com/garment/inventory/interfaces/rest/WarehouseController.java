package com.garment.inventory.interfaces.rest;

import com.garment.common.interfaces.R;
import com.garment.inventory.application.warehouse.WarehouseAppService;
import com.garment.inventory.application.warehouse.dto.CreateWarehouseCommand;
import com.garment.inventory.application.warehouse.dto.WarehouseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 仓库管理接口
 */
@RestController
@RequestMapping("/api/inventory/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseAppService warehouseAppService;

    /**
     * 创建仓库
     */
    @PostMapping
    public R<WarehouseDTO> createWarehouse(@Valid @RequestBody CreateWarehouseCommand command) {
        return R.ok(warehouseAppService.createWarehouse(command));
    }

    /**
     * 更新仓库
     */
    @PutMapping("/{id}")
    public R<WarehouseDTO> updateWarehouse(@PathVariable Long id, @Valid @RequestBody CreateWarehouseCommand command) {
        return R.ok(warehouseAppService.updateWarehouse(id, command));
    }

    /**
     * 获取仓库详情
     */
    @GetMapping("/{id}")
    public R<WarehouseDTO> getWarehouse(@PathVariable Long id) {
        return R.ok(warehouseAppService.getWarehouse(id));
    }

    /**
     * 获取所有仓库
     */
    @GetMapping
    public R<List<WarehouseDTO>> listWarehouses(@RequestParam(required = false) Integer type) {
        if (type != null) {
            return R.ok(warehouseAppService.listWarehousesByType(type));
        }
        return R.ok(warehouseAppService.listWarehouses());
    }

    /**
     * 启用仓库
     */
    @PutMapping("/{id}/enable")
    public R<Void> enableWarehouse(@PathVariable Long id) {
        warehouseAppService.enableWarehouse(id);
        return R.ok();
    }

    /**
     * 禁用仓库
     */
    @PutMapping("/{id}/disable")
    public R<Void> disableWarehouse(@PathVariable Long id) {
        warehouseAppService.disableWarehouse(id);
        return R.ok();
    }
}