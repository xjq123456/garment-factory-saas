package com.garment.order.application.sales.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreateSalesOrderCommand {
    private Long customerId;
    private String customerName;
    private LocalDate deliveryDate;
    private String shippingAddress;
    private String remark;
    private List<SalesItemDTO> items;

    @Data
    public static class SalesItemDTO {
        private Long skuId;
        private String skuCode;
        private String color;
        private String size;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private String remark;
    }
}