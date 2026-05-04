package com.garment.style.application.sku.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateSkuCommand {
    @NotNull(message = "款式ID不能为空")
    private Long styleId;
    @NotBlank(message = "SKU编码不能为空")
    private String skuCode;
    private String color;
    private String colorCode;
    private String size;
    private String sizeType;
    private String barcode;
    private BigDecimal weight;
    private BigDecimal extraPrice;
}
