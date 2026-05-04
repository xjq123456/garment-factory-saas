package com.garment.order.application.sales;

import com.garment.common.domain.BizException;
import com.garment.common.domain.TenantContext;
import com.garment.common.interfaces.PageResult;
import com.garment.order.application.sales.dto.CreateSalesOrderCommand;
import com.garment.order.domain.sales.entity.SalesOrder;
import com.garment.order.domain.sales.entity.SalesOrderItem;
import com.garment.order.domain.sales.event.OrderCompletedEvent;
import com.garment.order.domain.sales.event.OrderShippedEvent;
import com.garment.order.domain.sales.repository.SalesOrderRepository;
import com.garment.order.domain.sales.vo.SalesOrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class SalesOrderAppService {

    private final SalesOrderRepository orderRepo;
    private static final AtomicLong SEQ = new AtomicLong(System.currentTimeMillis() % 10000);

    @Transactional
    public SalesOrder create(CreateSalesOrderCommand cmd) {
        String orderNo = "SO" + System.currentTimeMillis() + SEQ.incrementAndGet() % 1000;
        Long tenantId = TenantContext.getTenantId();
        SalesOrder order = new SalesOrder(null, tenantId, orderNo);
        order.setCustomerId(cmd.getCustomerId());
        order.setCustomerName(cmd.getCustomerName());
        order.setDeliveryDate(cmd.getDeliveryDate());
        order.setShippingAddress(cmd.getShippingAddress());
        order.setRemark(cmd.getRemark());
        if (cmd.getItems() != null) {
            cmd.getItems().forEach(itemCmd -> {
                SalesOrderItem item = new SalesOrderItem(null, null);
                item.setSkuId(itemCmd.getSkuId());
                item.setSkuCode(itemCmd.getSkuCode());
                item.setColor(itemCmd.getColor());
                item.setSize(itemCmd.getSize());
                item.setQuantity(itemCmd.getQuantity());
                item.setUnitPrice(itemCmd.getUnitPrice());
                item.setAmount(itemCmd.getQuantity().multiply(itemCmd.getUnitPrice()));
                item.setRemark(itemCmd.getRemark());
                order.addItem(item);
            });
        }
        return orderRepo.save(order);
    }

    @Transactional
    public void confirm(Long id) {
        SalesOrder order = orderRepo.findById(id)
                .orElseThrow(() -> BizException.of("销售单不存在"));
        order.confirm();
        orderRepo.save(order);
    }

    @Transactional
    public void ship(Long id) {
        SalesOrder order = orderRepo.findById(id)
                .orElseThrow(() -> BizException.of("销售单不存在"));
        order.ship();
        order.registerEvent(new OrderShippedEvent(order.getId(), order.getOrderNo(), order.getTenantId()));
        orderRepo.save(order);
    }

    @Transactional
    public void complete(Long id) {
        SalesOrder order = orderRepo.findById(id)
                .orElseThrow(() -> BizException.of("销售单不存在"));
        order.complete();
        order.registerEvent(new OrderCompletedEvent(order.getId(), order.getOrderNo(), order.getTenantId()));
        orderRepo.save(order);
    }

    @Transactional
    public void cancel(Long id) {
        SalesOrder order = orderRepo.findById(id)
                .orElseThrow(() -> BizException.of("销售单不存在"));
        order.cancel();
        orderRepo.save(order);
    }

    public SalesOrder findById(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> BizException.of("销售单不存在"));
    }

    public PageResult<SalesOrder> findPage(int pageNum, int pageSize, String keyword, SalesOrderStatus status) {
        return orderRepo.findPage(pageNum, pageSize, keyword, status);
    }

    @Transactional
    public void delete(Long id) {
        SalesOrder order = orderRepo.findById(id)
                .orElseThrow(() -> BizException.of("销售单不存在"));
        if (order.getStatus() != SalesOrderStatus.DRAFT) {
            throw BizException.of("只有草稿状态的销售单才能删除");
        }
        orderRepo.deleteById(id);
    }
}