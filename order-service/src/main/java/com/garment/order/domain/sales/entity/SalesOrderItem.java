package com.garment.order.domain.sales.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SalesOrderItem {
    private Long id;
    private Long orderId;
    private Long skuId;
    private String skuCode;
    private String color;
    private String size;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal amount;
    private String remark;

    public SalesOrderItem(Long id, Long orderId) {
        this.id = id;
        this.orderId = orderId;
        this.quantity = BigDecimal.ZERO;
    }
}
