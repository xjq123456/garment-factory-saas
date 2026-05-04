package com.garment.style.domain.bom.entity;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class BomItem {

    private Long id;
    private Long tenantId;
    private Long bomId;
    private String materialName;
    private String materialCode;
    private String materialType;
    private String specification;
    private String unit;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private String supplier;
    private String color;
    private String remark;
    private Integer sortOrder;

    private BomItem() {}

    public static BomItem create(Long tenantId, Long bomId, String materialName,
                                 String materialCode, String materialType,
                                 String specification, String unit,
                                 BigDecimal quantity, BigDecimal unitPrice,
                                 String supplier, String color,
                                 String remark, Integer sortOrder) {
        BomItem item = new BomItem();
        item.tenantId = tenantId;
        item.bomId = bomId;
        item.materialName = materialName;
        item.materialCode = materialCode;
        item.materialType = materialType;
        item.specification = specification;
        item.unit = unit;
        item.quantity = quantity;
        item.unitPrice = unitPrice;
        item.supplier = supplier;
        item.color = color;
        item.remark = remark;
        item.sortOrder = sortOrder != null ? sortOrder : 0;
        return item;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBomId(Long bomId) {
        this.bomId = bomId;
    }
}
