package com.garment.order.domain.order.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductionOrderItem {
    private Long id;
    private Long orderId;
    private Long skuId;
    private String skuCode;
    private String color;
    private String size;
    private BigDecimal quantity;
    private BigDecimal completedQuantity = BigDecimal.ZERO;
    private String remark;

    public ProductionOrderItem(Long id, Long orderId) {
        this.id = id;
        this.orderId = orderId;
        this.quantity = BigDecimal.ZERO;
        this.completedQuantity = BigDecimal.ZERO;
    }
}
