package com.garment.order.application.order;

import com.garment.common.domain.BizException;
import com.garment.common.domain.TenantContext;
import com.garment.common.interfaces.PageResult;
import com.garment.order.application.order.dto.CreateProductionOrderCommand;
import com.garment.order.domain.order.entity.ProductionOrder;
import com.garment.order.domain.order.entity.ProductionOrderItem;
import com.garment.order.domain.order.repository.ProductionOrderRepository;
import com.garment.order.domain.order.vo.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class ProductionOrderAppService {

    private final ProductionOrderRepository orderRepo;
    private static final AtomicLong SEQ = new AtomicLong(System.currentTimeMillis() % 10000);

    @Transactional
    public ProductionOrder create(CreateProductionOrderCommand cmd) {
        String orderNo = "PO" + System.currentTimeMillis() + SEQ.incrementAndGet() % 1000;
        Long tenantId = TenantContext.getTenantId();
        ProductionOrder order = new ProductionOrder(null, tenantId, orderNo);
        order.setCustomerId(cmd.getCustomerId());
        order.setCustomerName(cmd.getCustomerName());
        order.setStyleId(cmd.getStyleId());
        order.setStyleName(cmd.getStyleName());
        order.setUnitPrice(cmd.getUnitPrice());
        order.setUnit(cmd.getUnit() != null ? cmd.getUnit() : "件");
        order.setDeliveryDate(cmd.getDeliveryDate());
        order.setRemark(cmd.getRemark());
        if (cmd.getItems() != null) {
            cmd.getItems().forEach(itemCmd -> {
                ProductionOrderItem item = new ProductionOrderItem(null, null);
                item.setSkuId(itemCmd.getSkuId());
                item.setSkuCode(itemCmd.getSkuCode());
                item.setColor(itemCmd.getColor());
                item.setSize(itemCmd.getSize());
                item.setQuantity(itemCmd.getQuantity());
                item.setRemark(itemCmd.getRemark());
                order.addItem(item);
            });
        }
        return orderRepo.save(order);
    }

    @Transactional
    public void confirm(Long id) {
        ProductionOrder order = orderRepo.findById(id)
                .orElseThrow(() -> BizException.of("加工单不存在"));
        order.confirm();
        orderRepo.save(order);
    }

    @Transactional
    public void startProduction(Long id) {
        ProductionOrder order = orderRepo.findById(id)
                .orElseThrow(() -> BizException.of("加工单不存在"));
        order.startProduction();
        orderRepo.save(order);
    }

    @Transactional
    public void complete(Long id) {
        ProductionOrder order = orderRepo.findById(id)
                .orElseThrow(() -> BizException.of("加工单不存在"));
        order.complete();
        orderRepo.save(order);
    }

    @Transactional
    public void cancel(Long id) {
        ProductionOrder order = orderRepo.findById(id)
                .orElseThrow(() -> BizException.of("加工单不存在"));
        order.cancel();
        orderRepo.save(order);
    }

    public ProductionOrder findById(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> BizException.of("加工单不存在"));
    }

    public PageResult<ProductionOrder> findPage(int pageNum, int pageSize, String keyword, OrderStatus status) {
        return orderRepo.findPage(pageNum, pageSize, keyword, status);
    }

    @Transactional
    public void delete(Long id) {
        ProductionOrder order = orderRepo.findById(id)
                .orElseThrow(() -> BizException.of("加工单不存在"));
        if (order.getStatus() != OrderStatus.DRAFT) {
            throw BizException.of("只有草稿状态的加工单才能删除");
        }
        orderRepo.deleteById(id);
    }
}