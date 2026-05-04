package com.garment.inventory.application.stock;

import com.garment.common.domain.BizException;
import com.garment.inventory.application.stock.dto.*;
import com.garment.inventory.application.warehouse.WarehouseAppService;
import com.garment.inventory.domain.stock.entity.Stock;
import com.garment.inventory.domain.stock.repository.StockRepository;
import com.garment.inventory.domain.stocklog.entity.StockLog;
import com.garment.inventory.domain.stocklog.repository.StockLogRepository;
import com.garment.inventory.domain.stocklog.vo.ChangeType;
import com.garment.inventory.domain.warehouse.entity.Warehouse;
import com.garment.inventory.domain.warehouse.repository.WarehouseRepository;
import com.garment.inventory.infrastructure.persistence.warehouse.WarehouseConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 成品库存应用服务
 */
@Service
@RequiredArgsConstructor
@Validated
public class StockAppService {

    private final StockRepository stockRepository;
    private final StockLogRepository stockLogRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseConverter warehouseConverter;

    /**
     * 入库
     */
    @Transactional(rollbackFor = Exception.class)
    public StockDTO inbound(InboundCommand command) {
        // 验证仓库
        Warehouse warehouse = warehouseRepository.findById(command.getWarehouseId())
                .orElseThrow(() -> new BizException("仓库不存在"));
        if (!warehouse.isEnabled()) {
            throw new BizException("仓库已禁用");
        }

        // 查找或创建库存记录
        Stock stock = stockRepository.findByWarehouseIdAndSkuId(command.getWarehouseId(), command.getSkuId())
                .orElseGet(() -> {
                    Stock newStock = Stock.create(
                            command.getWarehouseId(),
                            command.getSkuId(),
                            command.getStyleCode(),
                            command.getColor(),
                            command.getSize()
                    );
                    newStock.setStyleId(command.getStyleId());
                    return newStock;
                });

        int beforeQty = stock.getTotalQty();

        // 执行入库
        stock.inbound(command.getQuantity());

        // 保存或更新库存
        if (stock.getId() == null) {
            stockRepository.save(stock);
        } else {
            stockRepository.update(stock);
        }

        // 记录日志
        StockLog log = StockLog.create(
                command.getWarehouseId(),
                command.getSkuId(),
                ChangeType.INBOUND,
                command.getQuantity(),
                beforeQty,
                stock.getTotalQty(),
                command.getBizType(),
                command.getBizNo(),
                null,
                null,
                command.getRemark()
        );
        stockLogRepository.save(log);

        return toDTO(stock, warehouse.getWarehouseName());
    }

    /**
     * 出库
     */
    @Transactional(rollbackFor = Exception.class)
    public StockDTO outbound(OutboundCommand command) {
        // 验证仓库
        Warehouse warehouse = warehouseRepository.findById(command.getWarehouseId())
                .orElseThrow(() -> new BizException("仓库不存在"));

        // 查找库存记录
        Stock stock = stockRepository.findByWarehouseIdAndSkuId(command.getWarehouseId(), command.getSkuId())
                .orElseThrow(() -> new BizException("库存记录不存在"));

        int beforeQty = stock.getTotalQty();

        // 执行出库
        stock.outbound(command.getQuantity());

        // 更新库存
        stockRepository.update(stock);

        // 记录日志
        StockLog log = StockLog.create(
                command.getWarehouseId(),
                command.getSkuId(),
                ChangeType.OUTBOUND,
                -command.getQuantity(),
                beforeQty,
                stock.getTotalQty(),
                command.getBizType(),
                command.getBizNo(),
                null,
                null,
                command.getRemark()
        );
        stockLogRepository.save(log);

        return toDTO(stock, warehouse.getWarehouseName());
    }

    /**
     * 调拨
     */
    @Transactional(rollbackFor = Exception.class)
    public void transfer(TransferCommand command) {
        if (command.getFromWarehouseId().equals(command.getToWarehouseId())) {
            throw new BizException("源仓库和目标仓库不能相同");
        }

        // 验证源仓库
        Warehouse fromWarehouse = warehouseRepository.findById(command.getFromWarehouseId())
                .orElseThrow(() -> new BizException("源仓库不存在"));

        // 验证目标仓库
        Warehouse toWarehouse = warehouseRepository.findById(command.getToWarehouseId())
                .orElseThrow(() -> new BizException("目标仓库不存在"));

        // 源仓库出库
        Stock fromStock = stockRepository.findByWarehouseIdAndSkuId(command.getFromWarehouseId(), command.getSkuId())
                .orElseThrow(() -> new BizException("源仓库库存记录不存在"));

        int fromBeforeQty = fromStock.getTotalQty();
        fromStock.outbound(command.getQuantity());
        stockRepository.update(fromStock);

        // 记录源仓库出库日志
        StockLog outLog = StockLog.create(
                command.getFromWarehouseId(),
                command.getSkuId(),
                ChangeType.TRANSFER_OUT,
                -command.getQuantity(),
                fromBeforeQty,
                fromStock.getTotalQty(),
                "调拨",
                command.getBizNo(),
                null,
                null,
                command.getRemark()
        );
        stockLogRepository.save(outLog);

        // 目标仓库入库
        Stock toStock = stockRepository.findByWarehouseIdAndSkuId(command.getToWarehouseId(), command.getSkuId())
                .orElseGet(() -> {
                    Stock newStock = Stock.create(
                            command.getToWarehouseId(),
                            command.getSkuId(),
                            fromStock.getStyleCode(),
                            fromStock.getColor(),
                            fromStock.getSize()
                    );
                    newStock.setStyleId(fromStock.getStyleId());
                    return newStock;
                });

        int toBeforeQty = toStock.getTotalQty();
        toStock.inbound(command.getQuantity());

        if (toStock.getId() == null) {
            stockRepository.save(toStock);
        } else {
            stockRepository.update(toStock);
        }

        // 记录目标仓库入库日志
        StockLog inLog = StockLog.create(
                command.getToWarehouseId(),
                command.getSkuId(),
                ChangeType.TRANSFER_IN,
                command.getQuantity(),
                toBeforeQty,
                toStock.getTotalQty(),
                "调拨",
                command.getBizNo(),
                null,
                null,
                command.getRemark()
        );
        stockLogRepository.save(inLog);
    }

    /**
     * 锁定库存
     */
    @Transactional(rollbackFor = Exception.class)
    public StockDTO lockStock(Long warehouseId, Long skuId, int quantity, String bizNo, String remark) {
        Stock stock = stockRepository.findByWarehouseIdAndSkuId(warehouseId, skuId)
                .orElseThrow(() -> new BizException("库存记录不存在"));

        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new BizException("仓库不存在"));

        int beforeQty = stock.getAvailableQty();
        stock.lock(quantity);
        stockRepository.update(stock);

        // 记录日志
        StockLog log = StockLog.create(
                warehouseId,
                skuId,
                ChangeType.LOCK,
                quantity,
                beforeQty,
                stock.getAvailableQty(),
                "锁定",
                bizNo,
                null,
                null,
                remark
        );
        stockLogRepository.save(log);

        return toDTO(stock, warehouse.getWarehouseName());
    }

    /**
     * 解锁库存
     */
    @Transactional(rollbackFor = Exception.class)
    public StockDTO unlockStock(Long warehouseId, Long skuId, int quantity, String bizNo, String remark) {
        Stock stock = stockRepository.findByWarehouseIdAndSkuId(warehouseId, skuId)
                .orElseThrow(() -> new BizException("库存记录不存在"));

        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new BizException("仓库不存在"));

        int beforeQty = stock.getAvailableQty();
        stock.unlock(quantity);
        stockRepository.update(stock);

        // 记录日志
        StockLog log = StockLog.create(
                warehouseId,
                skuId,
                ChangeType.UNLOCK,
                quantity,
                beforeQty,
                stock.getAvailableQty(),
                "解锁",
                bizNo,
                null,
                null,
                remark
        );
        stockLogRepository.save(log);

        return toDTO(stock, warehouse.getWarehouseName());
    }

    /**
     * 查询库存详情
     */
    public StockDTO getStock(Long warehouseId, Long skuId) {
        Stock stock = stockRepository.findByWarehouseIdAndSkuId(warehouseId, skuId)
                .orElseThrow(() -> new BizException("库存记录不存在"));

        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new BizException("仓库不存在"));

        return toDTO(stock, warehouse.getWarehouseName());
    }

    /**
     * 查询SKU在所有仓库的库存
     */
    public List<StockDTO> getStockBySku(Long skuId) {
        List<Stock> stocks = stockRepository.findBySkuId(skuId);
        return stocks.stream().map(stock -> {
            String warehouseName = warehouseRepository.findById(stock.getWarehouseId())
                    .map(Warehouse::getWarehouseName)
                    .orElse("未知仓库");
            return toDTO(stock, warehouseName);
        }).collect(Collectors.toList());
    }

    /**
     * 查询仓库所有库存
     */
    public List<StockDTO> getStockByWarehouse(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new BizException("仓库不存在"));

        List<Stock> stocks = stockRepository.findByWarehouseId(warehouseId);
        return stocks.stream()
                .map(stock -> toDTO(stock, warehouse.getWarehouseName()))
                .collect(Collectors.toList());
    }

    /**
     * 查询款号库存
     */
    public List<StockDTO> getStockByStyleCode(String styleCode) {
        List<Stock> stocks = stockRepository.findByStyleCode(styleCode);
        return stocks.stream().map(stock -> {
            String warehouseName = warehouseRepository.findById(stock.getWarehouseId())
                    .map(Warehouse::getWarehouseName)
                    .orElse("未知仓库");
            return toDTO(stock, warehouseName);
        }).collect(Collectors.toList());
    }

    /**
     * 查询低于安全库存的库存
     */
    public List<StockDTO> getBelowSafetyStock() {
        List<Stock> stocks = stockRepository.findBelowSafetyStock();
        return stocks.stream().map(stock -> {
            String warehouseName = warehouseRepository.findById(stock.getWarehouseId())
                    .map(Warehouse::getWarehouseName)
                    .orElse("未知仓库");
            return toDTO(stock, warehouseName);
        }).collect(Collectors.toList());
    }

    /**
     * 查询库存变动日志
     */
    public List<StockLogDTO> getStockLogs(Long warehouseId, Long skuId) {
        List<StockLog> logs;
        if (warehouseId != null) {
            logs = stockLogRepository.findByWarehouseIdAndSkuId(warehouseId, skuId);
        } else {
            logs = stockLogRepository.findBySkuId(skuId);
        }
        return logs.stream().map(this::toLogDTO).collect(Collectors.toList());
    }

    /**
     * 根据业务单号查询库存变动日志
     */
    public List<StockLogDTO> getStockLogsByBizNo(String bizNo) {
        return stockLogRepository.findByBizNo(bizNo).stream()
                .map(this::toLogDTO)
                .collect(Collectors.toList());
    }

    private StockDTO toDTO(Stock stock, String warehouseName) {
        StockDTO dto = new StockDTO();
        dto.setId(stock.getId());
        dto.setWarehouseId(stock.getWarehouseId());
        dto.setWarehouseName(warehouseName);
        dto.setSkuId(stock.getSkuId());
        dto.setStyleId(stock.getStyleId());
        dto.setStyleCode(stock.getStyleCode());
        dto.setColor(stock.getColor());
        dto.setSize(stock.getSize());
        dto.setTotalQty(stock.getTotalQty());
        dto.setAvailableQty(stock.getAvailableQty());
        dto.setLockedQty(stock.getLockedQty());
        dto.setSafetyStock(stock.getSafetyStock());
        dto.setUnit(stock.getUnit());
        dto.setBelowSafetyStock(stock.isBelowSafetyStock());
        dto.setRemark(stock.getRemark());
        dto.setCreateTime(stock.getCreateTime());
        dto.setUpdateTime(stock.getUpdateTime());
        return dto;
    }

    private StockLogDTO toLogDTO(StockLog log) {
        StockLogDTO dto = new StockLogDTO();
        dto.setId(log.getId());
        dto.setWarehouseId(log.getWarehouseId());
        dto.setSkuId(log.getSkuId());
        dto.setChangeType(log.getChangeType() != null ? log.getChangeType().getCode() : null);
        dto.setChangeTypeDesc(log.getChangeType() != null ? log.getChangeType().getDesc() : null);
        dto.setChangeQty(log.getChangeQty());
        dto.setBeforeQty(log.getBeforeQty());
        dto.setAfterQty(log.getAfterQty());
        dto.setBizType(log.getBizType());
        dto.setBizNo(log.getBizNo());
        dto.setOperatorId(log.getOperatorId());
        dto.setOperatorName(log.getOperatorName());
        dto.setRemark(log.getRemark());
        dto.setCreateTime(log.getCreateTime());
        return dto;
    }
}