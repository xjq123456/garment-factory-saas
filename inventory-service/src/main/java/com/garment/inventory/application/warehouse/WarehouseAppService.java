package com.garment.inventory.application.warehouse;

import com.garment.common.domain.BizException;
import com.garment.inventory.application.warehouse.dto.CreateWarehouseCommand;
import com.garment.inventory.application.warehouse.dto.WarehouseDTO;
import com.garment.inventory.domain.warehouse.entity.Warehouse;
import com.garment.inventory.domain.warehouse.repository.WarehouseRepository;
import com.garment.inventory.domain.warehouse.vo.WarehouseType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 仓库应用服务
 */
@Service
@RequiredArgsConstructor
@Validated
public class WarehouseAppService {

    private final WarehouseRepository warehouseRepository;

    /**
     * 创建仓库
     */
    @Transactional(rollbackFor = Exception.class)
    public WarehouseDTO createWarehouse(CreateWarehouseCommand command) {
        // 检查仓库编码是否已存在
        warehouseRepository.findByCode(command.getWarehouseCode())
                .ifPresent(w -> {
                    throw new BizException("仓库编码已存在: " + command.getWarehouseCode());
                });

        Warehouse warehouse = Warehouse.create(
                command.getWarehouseCode(),
                command.getWarehouseName(),
                WarehouseType.of(command.getWarehouseType())
        );
        warehouse.setContactPerson(command.getContactPerson());
        warehouse.setContactPhone(command.getContactPhone());
        warehouse.setAddress(command.getAddress());
        warehouse.setRemark(command.getRemark());

        warehouseRepository.save(warehouse);
        return toDTO(warehouse);
    }

    /**
     * 更新仓库
     */
    @Transactional(rollbackFor = Exception.class)
    public WarehouseDTO updateWarehouse(Long id, CreateWarehouseCommand command) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new BizException("仓库不存在"));

        warehouse.setWarehouseName(command.getWarehouseName());
        warehouse.setWarehouseType(WarehouseType.of(command.getWarehouseType()));
        warehouse.setContactPerson(command.getContactPerson());
        warehouse.setContactPhone(command.getContactPhone());
        warehouse.setAddress(command.getAddress());
        warehouse.setRemark(command.getRemark());

        warehouseRepository.update(warehouse);
        return toDTO(warehouse);
    }

    /**
     * 获取仓库详情
     */
    public WarehouseDTO getWarehouse(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new BizException("仓库不存在"));
        return toDTO(warehouse);
    }

    /**
     * 获取所有仓库
     */
    public List<WarehouseDTO> listWarehouses() {
        return warehouseRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 根据类型查询仓库
     */
    public List<WarehouseDTO> listWarehousesByType(Integer type) {
        return warehouseRepository.findByType(type).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 启用仓库
     */
    @Transactional(rollbackFor = Exception.class)
    public void enableWarehouse(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new BizException("仓库不存在"));
        warehouse.enable();
        warehouseRepository.update(warehouse);
    }

    /**
     * 禁用仓库
     */
    @Transactional(rollbackFor = Exception.class)
    public void disableWarehouse(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new BizException("仓库不存在"));
        warehouse.disable();
        warehouseRepository.update(warehouse);
    }

    private WarehouseDTO toDTO(Warehouse warehouse) {
        WarehouseDTO dto = new WarehouseDTO();
        dto.setId(warehouse.getId());
        dto.setWarehouseCode(warehouse.getWarehouseCode());
        dto.setWarehouseName(warehouse.getWarehouseName());
        dto.setWarehouseType(warehouse.getWarehouseType() != null ? warehouse.getWarehouseType().getCode() : null);
        dto.setWarehouseTypeDesc(warehouse.getWarehouseType() != null ? warehouse.getWarehouseType().getDesc() : null);
        dto.setContactPerson(warehouse.getContactPerson());
        dto.setContactPhone(warehouse.getContactPhone());
        dto.setAddress(warehouse.getAddress());
        dto.setStatus(warehouse.getStatus() != null ? warehouse.getStatus().getCode() : null);
        dto.setStatusDesc(warehouse.getStatus() != null ? warehouse.getStatus().getDesc() : null);
        dto.setRemark(warehouse.getRemark());
        dto.setCreateTime(warehouse.getCreateTime());
        dto.setUpdateTime(warehouse.getUpdateTime());
        return dto;
    }
}