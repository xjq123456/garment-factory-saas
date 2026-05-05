package com.garment.inventory.infrastructure.persistence.stocklog;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.garment.common.domain.AuthUserContext;
import com.garment.inventory.domain.stocklog.entity.MaterialStockLog;
import com.garment.inventory.domain.stocklog.repository.MaterialStockLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 原材料库存变动日志仓储实现
 */
@Repository
@RequiredArgsConstructor
public class MaterialStockLogRepositoryImpl implements MaterialStockLogRepository {

    private final MaterialStockLogMapper materialStockLogMapper;
    private final MaterialStockLogConverter materialStockLogConverter;

    @Override
    public void save(MaterialStockLog log) {
        MaterialStockLogDO DO = materialStockLogConverter.toDO(log);
        DO.setTenantId(AuthUserContext.getTenantId());
        materialStockLogMapper.insert(DO);
    }

    @Override
    public void saveBatch(List<MaterialStockLog> logs) {
        for (MaterialStockLog log : logs) {
            save(log);
        }
    }

    @Override
    public List<MaterialStockLog> findByMaterialId(Long materialId) {
        LambdaQueryWrapper<MaterialStockLogDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MaterialStockLogDO::getTenantId, AuthUserContext.getTenantId())
                .eq(MaterialStockLogDO::getMaterialId, materialId)
                .orderByDesc(MaterialStockLogDO::getCreateTime);
        List<MaterialStockLogDO> list = materialStockLogMapper.selectList(wrapper);
        return list.stream().map(materialStockLogConverter::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<MaterialStockLog> findByWarehouseIdAndMaterialId(Long warehouseId, Long materialId) {
        LambdaQueryWrapper<MaterialStockLogDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MaterialStockLogDO::getTenantId, AuthUserContext.getTenantId())
                .eq(MaterialStockLogDO::getWarehouseId, warehouseId)
                .eq(MaterialStockLogDO::getMaterialId, materialId)
                .orderByDesc(MaterialStockLogDO::getCreateTime);
        List<MaterialStockLogDO> list = materialStockLogMapper.selectList(wrapper);
        return list.stream().map(materialStockLogConverter::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<MaterialStockLog> findByBizNo(String bizNo) {
        LambdaQueryWrapper<MaterialStockLogDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MaterialStockLogDO::getTenantId, AuthUserContext.getTenantId())
                .eq(MaterialStockLogDO::getBizNo, bizNo)
                .orderByDesc(MaterialStockLogDO::getCreateTime);
        List<MaterialStockLogDO> list = materialStockLogMapper.selectList(wrapper);
        return list.stream().map(materialStockLogConverter::toEntity).collect(Collectors.toList());
    }
}