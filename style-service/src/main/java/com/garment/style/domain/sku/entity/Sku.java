package com.garment.style.domain.sku.entity;

import com.garment.common.domain.AggregateRoot;
import com.garment.common.domain.DomainEvent;
import com.garment.style.domain.sku.event.SkuCreatedEvent;
import com.garment.style.domain.sku.event.SkuUpdatedEvent;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Sku extends AggregateRoot {

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

    /**
     * 创建SKU（工厂方法）。
     * <p>
     * 注意：此方法不产生领域事件，由应用层负责创建并发布 {@link SkuCreatedEvent}。
     */
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
        return s;
    }

    public DomainEvent update(String color, String colorCode, String size, String sizeType,
                       String barcode, BigDecimal weight, BigDecimal extraPrice) {
        this.color = color;
        this.colorCode = colorCode;
        this.size = size;
        this.sizeType = sizeType;
        this.barcode = barcode;
        this.weight = weight;
        this.extraPrice = extraPrice;
        return new SkuUpdatedEvent(this.tenantId, this.id, this.skuCode);
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

    /** 仅供基础设施层从数据库还原时使用，业务代码不应调用 */
    public void overrideStatus(Integer status) {
        this.status = status;
    }
}
