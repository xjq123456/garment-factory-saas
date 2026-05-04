package com.garment.inventory.infrastructure.persistence.stocklog;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.garment.common.domain.TenantContext;
import com.garment.inventory.domain.stocklog.entity.StockLog;
import com.garment.inventory.domain.stocklog.repository.StockLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 成品库存变动日志仓储实现
 */
@Repository
@RequiredArgsConstructor
public class StockLogRepositoryImpl implements StockLogRepository {

    private final StockLogMapper stockLogMapper;
    private final StockLogConverter stockLogConverter;

    @Override
    public void save(StockLog stockLog) {
        StockLogDO DO = stockLogConverter.toDO(stockLog);
        DO.setTenantId(TenantContext.getTenantId());
        stockLogMapper.insert(DO);
    }

    @Override
    public void saveBatch(List<StockLog> stockLogs) {
        for (StockLog stockLog : stockLogs) {
            save(stockLog);
        }
    }

    @Override
    public List<StockLog> findBySkuId(Long skuId) {
        LambdaQueryWrapper<StockLogDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockLogDO::getTenantId, TenantContext.getTenantId())
                .eq(StockLogDO::getSkuId, skuId)
                .orderByDesc(StockLogDO::getCreateTime);
        List<StockLogDO> list = stockLogMapper.selectList(wrapper);
        return list.stream().map(stockLogConverter::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<StockLog> findByWarehouseIdAndSkuId(Long warehouseId, Long skuId) {
        LambdaQueryWrapper<StockLogDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockLogDO::getTenantId, TenantContext.getTenantId())
                .eq(StockLogDO::getWarehouseId, warehouseId)
                .eq(StockLogDO::getSkuId, skuId)
                .orderByDesc(StockLogDO::getCreateTime);
        List<StockLogDO> list = stockLogMapper.selectList(wrapper);
        return list.stream().map(stockLogConverter::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<StockLog> findByBizNo(String bizNo) {
        LambdaQueryWrapper<StockLogDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockLogDO::getTenantId, TenantContext.getTenantId())
                .eq(StockLogDO::getBizNo, bizNo)
                .orderByDesc(StockLogDO::getCreateTime);
        List<StockLogDO> list = stockLogMapper.selectList(wrapper);
        return list.stream().map(stockLogConverter::toEntity).collect(Collectors.toList());
    }
}