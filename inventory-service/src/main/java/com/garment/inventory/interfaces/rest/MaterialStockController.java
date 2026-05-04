package com.garment.inventory.interfaces.rest;

import com.garment.common.interfaces.R;
import com.garment.inventory.application.material.MaterialStockAppService;
import com.garment.inventory.application.material.dto.MaterialInboundCommand;
import com.garment.inventory.application.material.dto.MaterialOutboundCommand;
import com.garment.inventory.application.material.dto.MaterialStockDTO;
import com.garment.inventory.domain.stocklog.entity.MaterialStockLog;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 原材料库存管理接口
 */
@RestController
@RequestMapping("/api/inventory/materials")
@RequiredArgsConstructor
public class MaterialStockController {

    private final MaterialStockAppService materialStockAppService;

    /**
     * 原材料入库
     */
    @PostMapping("/inbound")
    public R<MaterialStockDTO> inbound(@Valid @RequestBody MaterialInboundCommand command) {
        return R.ok(materialStockAppService.inbound(command));
    }

    /**
     * 原材料出库
     */
    @PostMapping("/outbound")
    public R<MaterialStockDTO> outbound(@Valid @RequestBody MaterialOutboundCommand command) {
        return R.ok(materialStockAppService.outbound(command));
    }

    /**
     * 查询原材料在指定仓库的库存
     */
    @GetMapping("/{warehouseId}/{materialId}")
    public R<List<MaterialStockDTO>> getMaterialStock(@PathVariable Long warehouseId,
                                                       @PathVariable Long materialId) {
        return R.ok(materialStockAppService.getMaterialStock(warehouseId, materialId));
    }

    /**
     * 查询原材料在所有仓库的库存
     */
    @GetMapping("/material/{materialId}")
    public R<List<MaterialStockDTO>> getMaterialStockAll(@PathVariable Long materialId) {
        return R.ok(materialStockAppService.getMaterialStockAll(materialId));
    }

    /**
     * 查询仓库所有原材料库存
     */
    @GetMapping("/warehouse/{warehouseId}")
    public R<List<MaterialStockDTO>> getMaterialStockByWarehouse(@PathVariable Long warehouseId) {
        return R.ok(materialStockAppService.getMaterialStockByWarehouse(warehouseId));
    }

    /**
     * 查询低于安全库存的原材料
     */
    @GetMapping("/below-safety-stock")
    public R<List<MaterialStockDTO>> getBelowSafetyStock() {
        return R.ok(materialStockAppService.getBelowSafetyStock());
    }

    /**
     * 查询原材料库存变动日志
     */
    @GetMapping("/{materialId}/logs")
    public R<List<MaterialStockLog>> getMaterialStockLogs(@PathVariable Long materialId,
                                                           @RequestParam(required = false) Long warehouseId) {
        return R.ok(materialStockAppService.getMaterialStockLogs(warehouseId, materialId));
    }
}