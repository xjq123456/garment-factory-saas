package com.garment.order.infrastructure.persistence.sales;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garment.common.interfaces.PageResult;
import com.garment.order.domain.sales.entity.SalesOrder;
import com.garment.order.domain.sales.entity.SalesOrderItem;
import com.garment.order.domain.sales.repository.SalesOrderRepository;
import com.garment.order.domain.sales.vo.SalesOrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SalesOrderRepositoryImpl implements SalesOrderRepository {

    private final SalesOrderMapper orderMapper;
    private final SalesOrderItemMapper itemMapper;

    @Override
    @Transactional
    public SalesOrder save(SalesOrder order) {
        SalesOrderDO orderDO = SalesOrderConverter.toDO(order);
        if (order.getId() == null) {
            orderMapper.insert(orderDO);
            order.setId(orderDO.getId());
        } else {
            orderMapper.updateById(orderDO);
        }
        LambdaQueryWrapper<SalesOrderItemDO> qw = new LambdaQueryWrapper<>();
        qw.eq(SalesOrderItemDO::getOrderId, order.getId());
        itemMapper.delete(qw);
        for (SalesOrderItem item : order.getItems()) {
            item.setOrderId(order.getId());
            SalesOrderItemDO itemDO = SalesOrderConverter.toItemDO(item);
            itemDO.setTenantId(order.getTenantId());
            itemMapper.insert(itemDO);
            item.setId(itemDO.getId());
        }
        return order;
    }

    @Override
    public Optional<SalesOrder> findById(Long id) {
        SalesOrderDO orderDO = orderMapper.selectById(id);
        if (orderDO == null) return Optional.empty();
        SalesOrder order = SalesOrderConverter.toDomain(orderDO);
        LambdaQueryWrapper<SalesOrderItemDO> qw = new LambdaQueryWrapper<>();
        qw.eq(SalesOrderItemDO::getOrderId, id);
        List<SalesOrderItemDO> itemDOs = itemMapper.selectList(qw);
        itemDOs.forEach(d -> order.addItem(SalesOrderConverter.toItemDomain(d)));
        return Optional.of(order);
    }

    @Override
    public Optional<SalesOrder> findByOrderNo(String orderNo) {
        LambdaQueryWrapper<SalesOrderDO> qw = new LambdaQueryWrapper<>();
        qw.eq(SalesOrderDO::getOrderNo, orderNo);
        SalesOrderDO orderDO = orderMapper.selectOne(qw);
        if (orderDO == null) return Optional.empty();
        return findById(orderDO.getId());
    }

    @Override
    public PageResult<SalesOrder> findPage(int pageNum, int pageSize, String keyword, SalesOrderStatus status) {
        LambdaQueryWrapper<SalesOrderDO> qw = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            qw.and(w -> w.like(SalesOrderDO::getOrderNo, keyword)
                    .or().like(SalesOrderDO::getCustomerName, keyword));
        }
        if (status != null) {
            qw.eq(SalesOrderDO::getStatus, status);
        }
        qw.orderByDesc(SalesOrderDO::getCreatedAt);
        Page<SalesOrderDO> page = orderMapper.selectPage(new Page<>(pageNum, pageSize), qw);
        List<SalesOrder> orders = page.getRecords().stream()
                .map(SalesOrderConverter::toDomain)
                .collect(Collectors.toList());
        return new PageResult<>(orders, page.getTotal(), pageNum, pageSize);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        orderMapper.deleteById(id);
        LambdaQueryWrapper<SalesOrderItemDO> qw = new LambdaQueryWrapper<>();
        qw.eq(SalesOrderItemDO::getOrderId, id);
        itemMapper.delete(qw);
    }
}