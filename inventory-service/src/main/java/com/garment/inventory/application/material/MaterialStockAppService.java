package com.garment.inventory.application.material;

import com.garment.common.domain.BizException;
import com.garment.inventory.application.material.dto.MaterialInboundCommand;
import com.garment.inventory.application.material.dto.MaterialOutboundCommand;
import com.garment.inventory.application.material.dto.MaterialStockDTO;
import com.garment.inventory.domain.material.entity.MaterialStock;
import com.garment.inventory.domain.material.repository.MaterialStockRepository;
import com.garment.inventory.domain.material.vo.MaterialType;
import com.garment.inventory.domain.stocklog.entity.MaterialStockLog;
import com.garment.inventory.domain.stocklog.repository.MaterialStockLogRepository;
import com.garment.inventory.domain.stocklog.vo.ChangeType;
import com.garment.inventory.domain.warehouse.entity.Warehouse;
import com.garment.inventory.domain.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 原材料库存应用服务
 */
@Service
@RequiredArgsConstructor
@Validated
public class MaterialStockAppService {

    private final MaterialStockRepository materialStockRepository;
    private final MaterialStockLogRepository materialStockLogRepository;
    private final WarehouseRepository warehouseRepository;

    /**
     * 原材料入库
     */
    @Transactional(rollbackFor = Exception.class)
    public MaterialStockDTO inbound(MaterialInboundCommand command) {
        // 验证仓库
        Warehouse warehouse = warehouseRepository.findById(command.getWarehouseId())
                .orElseThrow(() -> new BizException("仓库不存在"));
        if (!warehouse.isEnabled()) {
            throw new BizException("仓库已禁用");
        }

        // 查找或创建库存记录
        MaterialStock stock;
        if (command.getBatchNo() != null && !command.getBatchNo().isEmpty()) {
            stock = materialStockRepository.findByWarehouseIdAndMaterialIdAndBatchNo(
                            command.getWarehouseId(), command.getMaterialId(), command.getBatchNo())
                    .orElseGet(() -> MaterialStock.create(
                            command.getWarehouseId(),
                            command.getMaterialId(),
                            command.getMaterialCode(),
                            command.getMaterialName(),
                            command.getMaterialType() != null ? MaterialType.of(command.getMaterialType()) : null,
                            command.getUnit(),
                            command.getBatchNo()
                    ));
        } else {
            stock = materialStockRepository.findByWarehouseIdAndMaterialId(
                            command.getWarehouseId(), command.getMaterialId())
                    .orElseGet(() -> MaterialStock.create(
                            command.getWarehouseId(),
                            command.getMaterialId(),
                            command.getMaterialCode(),
                            command.getMaterialName(),
                            command.getMaterialType() != null ? MaterialType.of(command.getMaterialType()) : null,
                            command.getUnit(),
                            null
                    ));
        }

        BigDecimal beforeQty = stock.getTotalQty();

        // 执行入库
        stock.inbound(command.getQuantity());

        // 保存或更新库存
        if (stock.getId() == null) {
            materialStockRepository.save(stock);
        } else {
            materialStockRepository.update(stock);
        }

        // 记录日志
        MaterialStockLog log = MaterialStockLog.create(
                command.getWarehouseId(),
                command.getMaterialId(),
                ChangeType.INBOUND,
                command.getQuantity(),
                beforeQty,
                stock.getTotalQty(),
                command.getBizType(),
                command.getBizNo(),
                command.getBatchNo(),
                null,
                null,
                command.getRemark()
        );
        materialStockLogRepository.save(log);

        return toDTO(stock, warehouse.getWarehouseName());
    }

    /**
     * 原材料出库
     */
    @Transactional(rollbackFor = Exception.class)
    public MaterialStockDTO outbound(MaterialOutboundCommand command) {
        // 验证仓库
        Warehouse warehouse = warehouseRepository.findById(command.getWarehouseId())
                .orElseThrow(() -> new BizException("仓库不存在"));

        // 查找库存记录
        MaterialStock stock;
        if (command.getBatchNo() != null && !command.getBatchNo().isEmpty()) {
            stock = materialStockRepository.findByWarehouseIdAndMaterialIdAndBatchNo(
                            command.getWarehouseId(), command.getMaterialId(), command.getBatchNo())
                    .orElseThrow(() -> new BizException("库存记录不存在"));
        } else {
            stock = materialStockRepository.findByWarehouseIdAndMaterialId(
                            command.getWarehouseId(), command.getMaterialId())
                    .orElseThrow(() -> new BizException("库存记录不存在"));
        }

        BigDecimal beforeQty = stock.getTotalQty();

        // 执行出库
        stock.outbound(command.getQuantity());

        // 更新库存
        materialStockRepository.update(stock);

        // 记录日志
        MaterialStockLog log = MaterialStockLog.create(
                command.getWarehouseId(),
                command.getMaterialId(),
                ChangeType.OUTBOUND,
                command.getQuantity().negate(),
                beforeQty,
                stock.getTotalQty(),
                command.getBizType(),
                command.getBizNo(),
                command.getBatchNo(),
                null,
                null,
                command.getRemark()
        );
        materialStockLogRepository.save(log);

        return toDTO(stock, warehouse.getWarehouseName());
    }

    /**
     * 查询原材料在指定仓库的库存
     */
    public List<MaterialStockDTO> getMaterialStock(Long warehouseId, Long materialId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new BizException("仓库不存在"));

        List<MaterialStock> stocks = materialStockRepository.findByMaterialId(materialId);
        return stocks.stream()
                .filter(s -> s.getWarehouseId().equals(warehouseId))
                .map(stock -> toDTO(stock, warehouse.getWarehouseName()))
                .collect(Collectors.toList());
    }

    /**
     * 查询原材料在所有仓库的库存
     */
    public List<MaterialStockDTO> getMaterialStockAll(Long materialId) {
        List<MaterialStock> stocks = materialStockRepository.findByMaterialId(materialId);
        return stocks.stream().map(stock -> {
            String warehouseName = warehouseRepository.findById(stock.getWarehouseId())
                    .map(Warehouse::getWarehouseName)
                    .orElse("未知仓库");
            return toDTO(stock, warehouseName);
        }).collect(Collectors.toList());
    }

    /**
     * 查询仓库所有原材料库存
     */
    public List<MaterialStockDTO> getMaterialStockByWarehouse(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new BizException("仓库不存在"));

        List<MaterialStock> stocks = materialStockRepository.findByWarehouseId(warehouseId);
        return stocks.stream()
                .map(stock -> toDTO(stock, warehouse.getWarehouseName()))
                .collect(Collectors.toList());
    }

    /**
     * 查询低于安全库存的原材料
     */
    public List<MaterialStockDTO> getBelowSafetyStock() {
        List<MaterialStock> stocks = materialStockRepository.findBelowSafetyStock();
        return stocks.stream().map(stock -> {
            String warehouseName = warehouseRepository.findById(stock.getWarehouseId())
                    .map(Warehouse::getWarehouseName)
                    .orElse("未知仓库");
            return toDTO(stock, warehouseName);
        }).collect(Collectors.toList());
    }

    /**
     * 查询原材料库存变动日志
     */
    public List<MaterialStockLog> getMaterialStockLogs(Long warehouseId, Long materialId) {
        if (warehouseId != null) {
            return materialStockLogRepository.findByWarehouseIdAndMaterialId(warehouseId, materialId);
        }
        return materialStockLogRepository.findByMaterialId(materialId);
    }

    private MaterialStockDTO toDTO(MaterialStock stock, String warehouseName) {
        MaterialStockDTO dto = new MaterialStockDTO();
        dto.setId(stock.getId());
        dto.setWarehouseId(stock.getWarehouseId());
        dto.setWarehouseName(warehouseName);
        dto.setMaterialId(stock.getMaterialId());
        dto.setMaterialCode(stock.getMaterialCode());
        dto.setMaterialName(stock.getMaterialName());
        dto.setMaterialType(stock.getMaterialType() != null ? stock.getMaterialType().getCode() : null);
        dto.setMaterialTypeDesc(stock.getMaterialType() != null ? stock.getMaterialType().getDesc() : null);
        dto.setTotalQty(stock.getTotalQty());
        dto.setAvailableQty(stock.getAvailableQty());
        dto.setLockedQty(stock.getLockedQty());
        dto.setSafetyStock(stock.getSafetyStock());
        dto.setUnit(stock.getUnit());
        dto.setBatchNo(stock.getBatchNo());
        dto.setBelowSafetyStock(stock.isBelowSafetyStock());
        dto.setRemark(stock.getRemark());
        dto.setCreateTime(stock.getCreateTime());
        dto.setUpdateTime(stock.getUpdateTime());
        return dto;
    }
}