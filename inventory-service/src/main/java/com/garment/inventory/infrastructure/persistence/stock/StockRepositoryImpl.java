package com.garment.inventory.infrastructure.persistence.stock;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.garment.common.domain.TenantContext;
import com.garment.inventory.domain.stock.entity.Stock;
import com.garment.inventory.domain.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 成品库存仓储实现
 */
@Repository
@RequiredArgsConstructor
public class StockRepositoryImpl implements StockRepository {

    private final StockMapper stockMapper;
    private final StockConverter stockConverter;

    @Override
    public void save(Stock stock) {
        StockDO DO = stockConverter.toDO(stock);
        DO.setTenantId(TenantContext.getTenantId());
        stockMapper.insert(DO);
        stock.setId(DO.getId());
    }

    @Override
    public void update(Stock stock) {
        StockDO DO = stockConverter.toDO(stock);
        stockMapper.updateById(DO);
    }

    @Override
    public Optional<Stock> findById(Long id) {
        StockDO DO = stockMapper.selectById(id);
        return Optional.ofNullable(DO).map(stockConverter::toEntity);
    }

    @Override
    public Optional<Stock> findByWarehouseIdAndSkuId(Long warehouseId, Long skuId) {
        LambdaQueryWrapper<StockDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockDO::getTenantId, TenantContext.getTenantId())
                .eq(StockDO::getWarehouseId, warehouseId)
                .eq(StockDO::getSkuId, skuId);
        StockDO DO = stockMapper.selectOne(wrapper);
        return Optional.ofNullable(DO).map(stockConverter::toEntity);
    }

    @Override
    public List<Stock> findBySkuId(Long skuId) {
        LambdaQueryWrapper<StockDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockDO::getTenantId, TenantContext.getTenantId())
                .eq(StockDO::getSkuId, skuId);
        List<StockDO> list = stockMapper.selectList(wrapper);
        return list.stream().map(stockConverter::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<Stock> findByWarehouseId(Long warehouseId) {
        LambdaQueryWrapper<StockDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockDO::getTenantId, TenantContext.getTenantId())
                .eq(StockDO::getWarehouseId, warehouseId);
        List<StockDO> list = stockMapper.selectList(wrapper);
        return list.stream().map(stockConverter::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<Stock> findByStyleCode(String styleCode) {
        LambdaQueryWrapper<StockDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockDO::getTenantId, TenantContext.getTenantId())
                .eq(StockDO::getStyleCode, styleCode);
        List<StockDO> list = stockMapper.selectList(wrapper);
        return list.stream().map(stockConverter::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<Stock> findBelowSafetyStock() {
        Long tenantId = TenantContext.getTenantId();
        List<StockDO> list = stockMapper.selectBelowSafetyStock(tenantId);
        return list.stream().map(stockConverter::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<Stock> findByWarehouseIdAndSkuIds(Long warehouseId, List<Long> skuIds) {
        LambdaQueryWrapper<StockDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockDO::getTenantId, TenantContext.getTenantId())
                .eq(StockDO::getWarehouseId, warehouseId)
                .in(StockDO::getSkuId, skuIds);
        List<StockDO> list = stockMapper.selectList(wrapper);
        return list.stream().map(stockConverter::toEntity).collect(Collectors.toList());
    }
}