package com.garment.production.domain.order.repository;

import com.garment.common.interfaces.PageResult;
import com.garment.production.domain.order.entity.ProductionOrder;
import com.garment.production.domain.order.vo.OrderStatus;

import java.util.List;
import java.util.Optional;

/**
 * 生产工单仓储接口
 */
public interface ProductionOrderRepository {

    /**
     * 保存工单
     */
    void save(ProductionOrder order);

    /**
     * 更新工单
     */
    void update(ProductionOrder order);

    /**
     * 根据ID查找
     */
    Optional<ProductionOrder> findById(Long id);

    /**
     * 根据工单编号查找
     */
    Optional<ProductionOrder> findByOrderNo(String orderNo);

    /**
     * 分页查询
     */
    PageResult<ProductionOrder> findPage(String keyword, OrderStatus status,
                                          Integer priority, int page, int size);

    /**
     * 查询指定状态的工单列表
     */
    List<ProductionOrder> findByStatus(OrderStatus status);

    /**
     * 查询指定款式的工单列表
     */
    List<ProductionOrder> findByStyleId(Long styleId);

    /**
     * 查询逾期工单
     */
    List<ProductionOrder> findOverdueOrders();

    /**
     * 统计各状态工单数量
     */
    long countByStatus(OrderStatus status);
}