package com.garment.style.domain.sku.entity;

import com.garment.common.domain.AggregateRoot;
import com.garment.style.domain.sku.event.SkuCreatedEvent;
import com.garment.style.domain.sku.event.SkuUpdatedEvent;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Sku extends AggregateRoot {

    private Long id;
    private Long tenantId;
    private Long styleId;
    private String skuCode;
    private String color;
    private String colorCode;
    private String size;
    private String sizeType;
    private String barcode;
    private BigDecimal weight;
    /** SKU加价 */
    private BigDecimal extraPrice;
    /** 0=禁用 1=启用 */
    private Integer status;
    private Integer sortOrder;

    private Sku() {}

    public static Sku create(Long tenantId, Long styleId, String skuCode,
                             String color, String colorCode, String size,
                             String sizeType, String barcode,
                             BigDecimal weight, BigDecimal extraPrice) {
        Sku s = new Sku();
        s.tenantId = tenantId;
        s.styleId = styleId;
        s.skuCode = skuCode;
        s.color = color;
        s.colorCode = colorCode;
        s.size = size;
        s.sizeType = sizeType;
        s.barcode = barcode;
        s.weight = weight;
        s.extraPrice = extraPrice != null ? extraPrice : BigDecimal.ZERO;
        s.status = 1;
        s.sortOrder = 0;
        s.registerEvent(new SkuCreatedEvent(tenantId, styleId, skuCode));
        return s;
    }

    public void update(String color, String colorCode, String size, String sizeType,
                       String barcode, BigDecimal weight, BigDecimal extraPrice) {
        this.color = color;
        this.colorCode = colorCode;
        this.size = size;
        this.sizeType = sizeType;
        this.barcode = barcode;
        this.weight = weight;
        this.extraPrice = extraPrice;
        this.registerEvent(new SkuUpdatedEvent(this.tenantId, this.id, this.skuCode));
    }

    public void disable() {
        this.status = 0;
    }

    public void enable() {
        this.status = 1;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
