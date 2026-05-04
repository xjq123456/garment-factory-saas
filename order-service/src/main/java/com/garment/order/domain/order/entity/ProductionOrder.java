package com.garment.order.domain.order.entity;

import com.garment.common.domain.AggregateRoot;
import com.garment.order.domain.order.event.OrderConfirmedEvent;
import com.garment.order.domain.order.vo.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductionOrder extends AggregateRoot {

    private String orderNo;
    private Long customerId;
    private String customerName;
    private Long styleId;
    private String styleName;
    private BigDecimal totalQuantity;
    private String unit;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private LocalDate deliveryDate;
    private OrderStatus status;
    private String remark;

    private List<ProductionOrderItem> items = new ArrayList<>();

    public ProductionOrder(Long id, Long tenantId, String orderNo) {
        super(id, tenantId);
        this.orderNo = orderNo;
        this.status = OrderStatus.DRAFT;
        this.unit = "件";
        this.totalQuantity = BigDecimal.ZERO;
    }

    public void confirm() {
        if (status != OrderStatus.DRAFT) {
            throw new IllegalStateException("只有草稿状态的加工单才能确认");
        }
        this.status = OrderStatus.CONFIRMED;
        registerEvent(new OrderConfirmedEvent(this.getId(), this.getOrderNo(), getTenantId()));
    }

    public void startProduction() {
        if (status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("只有已确认的加工单才能开始生产");
        }
        this.status = OrderStatus.IN_PRODUCTION;
    }

    public void complete() {
        if (status != OrderStatus.IN_PRODUCTION) {
            throw new IllegalStateException("只有生产中的加工单才能完成");
        }
        this.status = OrderStatus.COMPLETED;
    }

    public void cancel() {
        if (status == OrderStatus.COMPLETED) {
            throw new IllegalStateException("已完成的加工单不能取消");
        }
        this.status = OrderStatus.CANCELLED;
    }

    public void addItem(ProductionOrderItem item) {
        this.items.add(item);
        recalculateTotalQuantity();
    }

    public void removeItem(Long itemId) {
        this.items.removeIf(i -> i.getId().equals(itemId));
        recalculateTotalQuantity();
    }

    private void recalculateTotalQuantity() {
        this.totalQuantity = items.stream()
                .map(ProductionOrderItem::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (this.unitPrice != null) {
            this.totalAmount = this.totalQuantity.multiply(this.unitPrice);
        }
    }

    public List<ProductionOrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void clearItems() {
        this.items.clear();
    }
}