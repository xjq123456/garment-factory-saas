package com.garment.style.application.sku.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateSkuCommand {
    private String color;
    private String colorCode;
    private String size;
    private String sizeType;
    private String barcode;
    private BigDecimal weight;
    private BigDecimal extraPrice;
}
