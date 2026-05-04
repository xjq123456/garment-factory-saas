package com.garment.inventory.infrastructure.persistence.warehouse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.garment.common.domain.TenantContext;
import com.garment.inventory.domain.warehouse.entity.Warehouse;
import com.garment.inventory.domain.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 仓储仓储实现
 */
@Repository
@RequiredArgsConstructor
public class WarehouseRepositoryImpl implements WarehouseRepository {

    private final WarehouseMapper warehouseMapper;
    private final WarehouseConverter warehouseConverter;

    @Override
    public void save(Warehouse warehouse) {
        WarehouseDO DO = warehouseConverter.toDO(warehouse);
        DO.setTenantId(TenantContext.getTenantId());
        warehouseMapper.insert(DO);
        warehouse.setId(DO.getId());
    }

    @Override
    public void update(Warehouse warehouse) {
        WarehouseDO DO = warehouseConverter.toDO(warehouse);
        warehouseMapper.updateById(DO);
    }

    @Override
    public Optional<Warehouse> findById(Long id) {
        WarehouseDO DO = warehouseMapper.selectById(id);
        return Optional.ofNullable(DO).map(warehouseConverter::toEntity);
    }

    @Override
    public Optional<Warehouse> findByCode(String code) {
        LambdaQueryWrapper<WarehouseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WarehouseDO::getTenantId, TenantContext.getTenantId())
                .eq(WarehouseDO::getWarehouseCode, code);
        WarehouseDO DO = warehouseMapper.selectOne(wrapper);
        return Optional.ofNullable(DO).map(warehouseConverter::toEntity);
    }

    @Override
    public List<Warehouse> findAll() {
        LambdaQueryWrapper<WarehouseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WarehouseDO::getTenantId, TenantContext.getTenantId());
        List<WarehouseDO> list = warehouseMapper.selectList(wrapper);
        return list.stream().map(warehouseConverter::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<Warehouse> findByType(int type) {
        LambdaQueryWrapper<WarehouseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WarehouseDO::getTenantId, TenantContext.getTenantId())
                .eq(WarehouseDO::getWarehouseType, type);
        List<WarehouseDO> list = warehouseMapper.selectList(wrapper);
        return list.stream().map(warehouseConverter::toEntity).collect(Collectors.toList());
    }
}