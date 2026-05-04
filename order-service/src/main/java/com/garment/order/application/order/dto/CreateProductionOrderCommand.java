package com.garment.order.application.order.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreateProductionOrderCommand {
    private Long customerId;
    private String customerName;
    private Long styleId;
    private String styleName;
    private BigDecimal unitPrice;
    private String unit;
    private LocalDate deliveryDate;
    private String remark;
    private List<OrderItemDTO> items;

    @Data
    public static class OrderItemDTO {
        private Long skuId;
        private String skuCode;
        private String color;
        private String size;
        private BigDecimal quantity;
        private String remark;
    }
}