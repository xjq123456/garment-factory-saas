package com.garment.order.domain.sales.repository;

import com.garment.common.interfaces.PageResult;
import com.garment.order.domain.sales.entity.SalesOrder;
import com.garment.order.domain.sales.vo.SalesOrderStatus;
import java.util.Optional;

public interface SalesOrderRepository {
    SalesOrder save(SalesOrder order);
    Optional<SalesOrder> findById(Long id);
    Optional<SalesOrder> findByOrderNo(String orderNo);
    PageResult<SalesOrder> findPage(int pageNum, int pageSize, String keyword, SalesOrderStatus status);
    void deleteById(Long id);
}
