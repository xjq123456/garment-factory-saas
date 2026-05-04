package com.garment.order.domain.order.repository;

import com.garment.common.interfaces.PageResult;
import com.garment.order.domain.order.entity.ProductionOrder;
import com.garment.order.domain.order.vo.OrderStatus;
import java.util.Optional;

public interface ProductionOrderRepository {
    ProductionOrder save(ProductionOrder order);
    Optional<ProductionOrder> findById(Long id);
    Optional<ProductionOrder> findByOrderNo(String orderNo);
    PageResult<ProductionOrder> findPage(int pageNum, int pageSize, String keyword, OrderStatus status);
    void deleteById(Long id);
}