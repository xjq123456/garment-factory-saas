package com.garment.inventory.infrastructure.persistence.material;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.garment.common.domain.AuthUserContext;
import com.garment.inventory.domain.material.entity.MaterialStock;
import com.garment.inventory.domain.material.repository.MaterialStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 原材料库存仓储实现
 */
@Repository
@RequiredArgsConstructor
public class MaterialStockRepositoryImpl implements MaterialStockRepository {

    private final MaterialStockMapper materialStockMapper;
    private final MaterialStockConverter materialStockConverter;

    @Override
    public void save(MaterialStock stock) {
        MaterialStockDO DO = materialStockConverter.toDO(stock);
        DO.setTenantId(AuthUserContext.getTenantId());
        materialStockMapper.insert(DO);
        stock.setId(DO.getId());
    }

    @Override
    public void update(MaterialStock stock) {
        MaterialStockDO DO = materialStockConverter.toDO(stock);
        materialStockMapper.updateById(DO);
    }

    @Override
    public Optional<MaterialStock> findById(Long id) {
        MaterialStockDO DO = materialStockMapper.selectById(id);
        return Optional.ofNullable(DO).map(materialStockConverter::toEntity);
    }

    @Override
    public Optional<MaterialStock> findByWarehouseIdAndMaterialId(Long warehouseId, Long materialId) {
        LambdaQueryWrapper<MaterialStockDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MaterialStockDO::getTenantId, AuthUserContext.getTenantId())
                .eq(MaterialStockDO::getWarehouseId, warehouseId)
                .eq(MaterialStockDO::getMaterialId, materialId);
        MaterialStockDO DO = materialStockMapper.selectOne(wrapper);
        return Optional.ofNullable(DO).map(materialStockConverter::toEntity);
    }

    @Override
    public Optional<MaterialStock> findByWarehouseIdAndMaterialIdAndBatchNo(Long warehouseId, Long materialId, String batchNo) {
        LambdaQueryWrapper<MaterialStockDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MaterialStockDO::getTenantId, AuthUserContext.getTenantId())
                .eq(MaterialStockDO::getWarehouseId, warehouseId)
                .eq(MaterialStockDO::getMaterialId, materialId)
                .eq(MaterialStockDO::getBatchNo, batchNo);
        MaterialStockDO DO = materialStockMapper.selectOne(wrapper);
        return Optional.ofNullable(DO).map(materialStockConverter::toEntity);
    }

    @Override
    public List<MaterialStock> findByMaterialId(Long materialId) {
        LambdaQueryWrapper<MaterialStockDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MaterialStockDO::getTenantId, AuthUserContext.getTenantId())
                .eq(MaterialStockDO::getMaterialId, materialId);
        List<MaterialStockDO> list = materialStockMapper.selectList(wrapper);
        return list.stream().map(materialStockConverter::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<MaterialStock> findByWarehouseId(Long warehouseId) {
        LambdaQueryWrapper<MaterialStockDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MaterialStockDO::getTenantId, AuthUserContext.getTenantId())
                .eq(MaterialStockDO::getWarehouseId, warehouseId);
        List<MaterialStockDO> list = materialStockMapper.selectList(wrapper);
        return list.stream().map(materialStockConverter::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<MaterialStock> findBelowSafetyStock() {
        Long tenantId = AuthUserContext.getTenantId();
        List<MaterialStockDO> list = materialStockMapper.selectBelowSafetyStock(tenantId);
        return list.stream().map(materialStockConverter::toEntity).collect(Collectors.toList());
    }
}