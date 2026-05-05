package com.garment.production.infrastructure.persistence.order;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garment.common.interfaces.PageResult;
import com.garment.production.domain.order.entity.ProductionOrder;
import com.garment.production.domain.order.repository.ProductionOrderRepository;
import com.garment.production.domain.order.vo.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 生产工单仓储实现
 */
@Repository
@RequiredArgsConstructor
public class ProductionOrderRepositoryImpl implements ProductionOrderRepository {

    private final ProductionOrderMapper mapper;
    private final ProductionOrderConverter converter;

    @Override
    public void save(ProductionOrder order) {
        ProductionOrderDO DO = converter.toDO(order);
        mapper.insert(DO);
        order.setId(DO.getId());
    }

    @Override
    public void update(ProductionOrder order) {
        mapper.updateById(converter.toDO(order));
    }

    @Override
    public Optional<ProductionOrder> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id))
                .map(converter::toEntity);
    }

    @Override
    public Optional<ProductionOrder> findByOrderNo(String orderNo) {
        LambdaQueryWrapper<ProductionOrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductionOrderDO::getOrderNo, orderNo);
        return Optional.ofNullable(mapper.selectOne(wrapper))
                .map(converter::toEntity);
    }

    @Override
    public PageResult<ProductionOrder> findPage(String keyword, OrderStatus status,
                                                 Integer priority, int page, int size) {
        LambdaQueryWrapper<ProductionOrderDO> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                    .like(ProductionOrderDO::getOrderNo, keyword)
                    .or()
                    .like(ProductionOrderDO::getStyleName, keyword)
                    .or()
                    .like(ProductionOrderDO::getCustomerName, keyword)
            );
        }
        if (status != null) {
            wrapper.eq(ProductionOrderDO::getStatus, status.getCode());
        }
        if (priority != null) {
            wrapper.eq(ProductionOrderDO::getPriority, priority);
        }
        wrapper.orderByDesc(ProductionOrderDO::getCreateTime);

        Page<ProductionOrderDO> pageResult = mapper.selectPage(new Page<>(page, size), wrapper);

        List<ProductionOrder> records = pageResult.getRecords().stream()
                .map(converter::toEntity)
                .collect(Collectors.toList());

        return new PageResult<>(records, pageResult.getTotal(), page, size);
    }

    @Override
    public List<ProductionOrder> findByStatus(OrderStatus status) {
        LambdaQueryWrapper<ProductionOrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductionOrderDO::getStatus, status.getCode());
        return mapper.selectList(wrapper).stream()
                .map(converter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrder> findByStyleId(Long styleId) {
        LambdaQueryWrapper<ProductionOrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductionOrderDO::getStyleId, styleId);
        return mapper.selectList(wrapper).stream()
                .map(converter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrder> findOverdueOrders() {
        LambdaQueryWrapper<ProductionOrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.lt(ProductionOrderDO::getDeliveryDate, LocalDate.now())
                .notIn(ProductionOrderDO::getStatus,
                        OrderStatus.COMPLETED.getCode(), OrderStatus.CLOSED.getCode());
        return mapper.selectList(wrapper).stream()
                .map(converter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public long countByStatus(OrderStatus status) {
        LambdaQueryWrapper<ProductionOrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductionOrderDO::getStatus, status.getCode());
        return mapper.selectCount(wrapper);
    }
}