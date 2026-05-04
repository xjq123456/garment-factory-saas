package com.garment.order.domain.sales.entity;

import com.garment.common.domain.AggregateRoot;
import com.garment.order.domain.sales.vo.SalesOrderStatus;
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
public class SalesOrder extends AggregateRoot {

    private String orderNo;
    private Long customerId;
    private String customerName;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private LocalDate deliveryDate;
    private SalesOrderStatus status;
    private String shippingAddress;
    private String remark;

    private List<SalesOrderItem> items = new ArrayList<>();

    public SalesOrder(Long id, Long tenantId, String orderNo) {
        super(id, tenantId);
        this.orderNo = orderNo;
        this.status = SalesOrderStatus.DRAFT;
        this.totalAmount = BigDecimal.ZERO;
        this.paidAmount = BigDecimal.ZERO;
    }

    public void confirm() {
        if (status != SalesOrderStatus.DRAFT) {
            throw new IllegalStateException("只有草稿状态的销售单才能确认");
        }
        this.status = SalesOrderStatus.CONFIRMED;
    }

    public void ship() {
        if (status != SalesOrderStatus.CONFIRMED) {
            throw new IllegalStateException("只有已确认的销售单才能发货");
        }
        this.status = SalesOrderStatus.SHIPPED;
    }

    public void complete() {
        if (status != SalesOrderStatus.SHIPPED) {
            throw new IllegalStateException("只有已发货的销售单才能完成");
        }
        this.status = SalesOrderStatus.COMPLETED;
    }

    public void cancel() {
        if (status == SalesOrderStatus.COMPLETED) {
            throw new IllegalStateException("已完成的销售单不能取消");
        }
        this.status = SalesOrderStatus.CANCELLED;
    }

    public void addItem(SalesOrderItem item) {
        this.items.add(item);
        recalculateTotalAmount();
    }

    public void removeItem(Long itemId) {
        this.items.removeIf(i -> i.getId().equals(itemId));
        recalculateTotalAmount();
    }

    private void recalculateTotalAmount() {
        this.totalAmount = items.stream()
                .map(SalesOrderItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<SalesOrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void clearItems() {
        this.items.clear();
    }
}