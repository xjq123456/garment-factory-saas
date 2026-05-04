package com.garment.order.infrastructure.persistence.order;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garment.common.interfaces.PageResult;
import com.garment.order.domain.order.entity.ProductionOrder;
import com.garment.order.domain.order.entity.ProductionOrderItem;
import com.garment.order.domain.order.repository.ProductionOrderRepository;
import com.garment.order.domain.order.vo.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductionOrderRepositoryImpl implements ProductionOrderRepository {

    private final ProductionOrderMapper orderMapper;
    private final ProductionOrderItemMapper itemMapper;

    @Override
    @Transactional
    public ProductionOrder save(ProductionOrder order) {
        ProductionOrderDO orderDO = ProductionOrderConverter.toDO(order);
        if (order.getId() == null) {
            orderMapper.insert(orderDO);
            order.setId(orderDO.getId());
        } else {
            orderMapper.updateById(orderDO);
        }
        // 保存明细：先删后插
        LambdaQueryWrapper<ProductionOrderItemDO> qw = new LambdaQueryWrapper<>();
        qw.eq(ProductionOrderItemDO::getOrderId, order.getId());
        itemMapper.delete(qw);
        for (ProductionOrderItem item : order.getItems()) {
            item.setOrderId(order.getId());
            ProductionOrderItemDO itemDO = ProductionOrderConverter.toItemDO(item);
            itemDO.setTenantId(order.getTenantId());
            itemMapper.insert(itemDO);
            item.setId(itemDO.getId());
        }
        return order;
    }

    @Override
    public Optional<ProductionOrder> findById(Long id) {
        ProductionOrderDO orderDO = orderMapper.selectById(id);
        if (orderDO == null) return Optional.empty();
        ProductionOrder order = ProductionOrderConverter.toDomain(orderDO);
        LambdaQueryWrapper<ProductionOrderItemDO> qw = new LambdaQueryWrapper<>();
        qw.eq(ProductionOrderItemDO::getOrderId, id);
        List<ProductionOrderItemDO> itemDOs = itemMapper.selectList(qw);
        itemDOs.forEach(d -> order.addItem(ProductionOrderConverter.toItemDomain(d)));
        return Optional.of(order);
    }

    @Override
    public Optional<ProductionOrder> findByOrderNo(String orderNo) {
        LambdaQueryWrapper<ProductionOrderDO> qw = new LambdaQueryWrapper<>();
        qw.eq(ProductionOrderDO::getOrderNo, orderNo);
        ProductionOrderDO orderDO = orderMapper.selectOne(qw);
        if (orderDO == null) return Optional.empty();
        return findById(orderDO.getId());
    }

    @Override
    public PageResult<ProductionOrder> findPage(int pageNum, int pageSize, String keyword, OrderStatus status) {
        LambdaQueryWrapper<ProductionOrderDO> qw = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            qw.and(w -> w.like(ProductionOrderDO::getOrderNo, keyword)
                    .or().like(ProductionOrderDO::getCustomerName, keyword)
                    .or().like(ProductionOrderDO::getStyleName, keyword));
        }
        if (status != null) {
            qw.eq(ProductionOrderDO::getStatus, status);
        }
        qw.orderByDesc(ProductionOrderDO::getCreatedAt);
        Page<ProductionOrderDO> page = orderMapper.selectPage(new Page<>(pageNum, pageSize), qw);
        List<ProductionOrder> orders = page.getRecords().stream()
                .map(ProductionOrderConverter::toDomain)
                .collect(Collectors.toList());
        return new PageResult<>(orders, page.getTotal());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        orderMapper.deleteById(id);
        LambdaQueryWrapper<ProductionOrderItemDO> qw = new LambdaQueryWrapper<>();
        qw.eq(ProductionOrderItemDO::getOrderId, id);
        itemMapper.delete(qw);
    }
}