package com.garment.inventory.interfaces.rest;

import com.garment.common.interfaces.R;
import com.garment.inventory.application.stock.StockAppService;
import com.garment.inventory.application.stock.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 成品库存管理接口
 */
@RestController
@RequestMapping("/api/inventory/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockAppService stockAppService;

    /**
     * 入库
     */
    @PostMapping("/inbound")
    public R<StockDTO> inbound(@Valid @RequestBody InboundCommand command) {
        return R.ok(stockAppService.inbound(command));
    }

    /**
     * 出库
     */
    @PostMapping("/outbound")
    public R<StockDTO> outbound(@Valid @RequestBody OutboundCommand command) {
        return R.ok(stockAppService.outbound(command));
    }

    /**
     * 调拨
     */
    @PostMapping("/transfer")
    public R<Void> transfer(@Valid @RequestBody TransferCommand command) {
        stockAppService.transfer(command);
        return R.ok();
    }

    /**
     * 锁定库存
     */
    @PostMapping("/{warehouseId}/{skuId}/lock")
    public R<StockDTO> lockStock(@PathVariable Long warehouseId,
                                  @PathVariable Long skuId,
                                  @RequestParam int quantity,
                                  @RequestParam(required = false) String bizNo,
                                  @RequestParam(required = false) String remark) {
        return R.ok(stockAppService.lockStock(warehouseId, skuId, quantity, bizNo, remark));
    }

    /**
     * 解锁库存
     */
    @PostMapping("/{warehouseId}/{skuId}/unlock")
    public R<StockDTO> unlockStock(@PathVariable Long warehouseId,
                                    @PathVariable Long skuId,
                                    @RequestParam int quantity,
                                    @RequestParam(required = false) String bizNo,
                                    @RequestParam(required = false) String remark) {
        return R.ok(stockAppService.unlockStock(warehouseId, skuId, quantity, bizNo, remark));
    }

    /**
     * 查询库存详情
     */
    @GetMapping("/{warehouseId}/{skuId}")
    public R<StockDTO> getStock(@PathVariable Long warehouseId, @PathVariable Long skuId) {
        return R.ok(stockAppService.getStock(warehouseId, skuId));
    }

    /**
     * 查询SKU在所有仓库的库存
     */
    @GetMapping("/sku/{skuId}")
    public R<List<StockDTO>> getStockBySku(@PathVariable Long skuId) {
        return R.ok(stockAppService.getStockBySku(skuId));
    }

    /**
     * 查询仓库所有库存
     */
    @GetMapping("/warehouse/{warehouseId}")
    public R<List<StockDTO>> getStockByWarehouse(@PathVariable Long warehouseId) {
        return R.ok(stockAppService.getStockByWarehouse(warehouseId));
    }

    /**
     * 查询款号库存
     */
    @GetMapping("/style/{styleCode}")
    public R<List<StockDTO>> getStockByStyleCode(@PathVariable String styleCode) {
        return R.ok(stockAppService.getStockByStyleCode(styleCode));
    }

    /**
     * 查询低于安全库存的库存
     */
    @GetMapping("/below-safety-stock")
    public R<List<StockDTO>> getBelowSafetyStock() {
        return R.ok(stockAppService.getBelowSafetyStock());
    }

    /**
     * 查询库存变动日志
     */
    @GetMapping("/{skuId}/logs")
    public R<List<StockLogDTO>> getStockLogs(@PathVariable Long skuId,
                                              @RequestParam(required = false) Long warehouseId) {
        return R.ok(stockAppService.getStockLogs(warehouseId, skuId));
    }

    /**
     * 根据业务单号查询库存变动日志
     */
    @GetMapping("/logs/biz-no/{bizNo}")
    public R<List<StockLogDTO>> getStockLogsByBizNo(@PathVariable String bizNo) {
        return R.ok(stockAppService.getStockLogsByBizNo(bizNo));
    }
}