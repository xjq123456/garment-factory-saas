package com.garment.inventory.domain.material.entity;

import com.garment.common.domain.AggregateRoot;
import com.garment.common.domain.BizException;
import com.garment.inventory.domain.material.vo.MaterialType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 原材料库存实体 - 聚合根
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaterialStock extends AggregateRoot {

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * 原材料ID
     */
    private Long materialId;

    /**
     * 原材料编码
     */
    private String materialCode;

    /**
     * 原材料名称
     */
    private String materialName;

    /**
     * 原材料类型
     */
    private MaterialType materialType;

    /**
     * 总库存数量
     */
    private BigDecimal totalQty;

    /**
     * 可用库存数量
     */
    private BigDecimal availableQty;

    /**
     * 锁定库存数量
     */
    private BigDecimal lockedQty;

    /**
     * 安全库存
     */
    private BigDecimal safetyStock;

    /**
     * 单位
     */
    private String unit;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 入库
     */
    public void inbound(BigDecimal qty) {
        if (qty == null || qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException("入库数量必须大于0");
        }
        this.totalQty = this.totalQty.add(qty);
        this.availableQty = this.availableQty.add(qty);
    }

    /**
     * 出库
     */
    public void outbound(BigDecimal qty) {
        if (qty == null || qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException("出库数量必须大于0");
        }
        if (this.availableQty.compareTo(qty) < 0) {
            throw new BizException("可用库存不足, 当前可用: " + this.availableQty + ", 需要: " + qty);
        }
        this.totalQty = this.totalQty.subtract(qty);
        this.availableQty = this.availableQty.subtract(qty);
    }

    /**
     * 锁定库存
     */
    public void lock(BigDecimal qty) {
        if (qty == null || qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException("锁定数量必须大于0");
        }
        if (this.availableQty.compareTo(qty) < 0) {
            throw new BizException("可用库存不足, 无法锁定");
        }
        this.availableQty = this.availableQty.subtract(qty);
        this.lockedQty = this.lockedQty.add(qty);
    }

    /**
     * 解锁库存
     */
    public void unlock(BigDecimal qty) {
        if (qty == null || qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException("解锁数量必须大于0");
        }
        if (this.lockedQty.compareTo(qty) < 0) {
            throw new BizException("锁定库存不足, 无法解锁");
        }
        this.availableQty = this.availableQty.add(qty);
        this.lockedQty = this.lockedQty.subtract(qty);
    }

    /**
     * 是否低于安全库存
     */
    public boolean isBelowSafetyStock() {
        return this.safetyStock != null
                && this.safetyStock.compareTo(BigDecimal.ZERO) > 0
                && this.totalQty.compareTo(this.safetyStock) < 0;
    }

    /**
     * 创建原材料库存记录
     */
    public static MaterialStock create(Long warehouseId, Long materialId, String materialCode,
                                        String materialName, MaterialType materialType, String unit, String batchNo) {
        MaterialStock stock = new MaterialStock();
        stock.setWarehouseId(warehouseId);
        stock.setMaterialId(materialId);
        stock.setMaterialCode(materialCode);
        stock.setMaterialName(materialName);
        stock.setMaterialType(materialType);
        stock.setTotalQty(BigDecimal.ZERO);
        stock.setAvailableQty(BigDecimal.ZERO);
        stock.setLockedQty(BigDecimal.ZERO);
        stock.setUnit(unit);
        stock.setBatchNo(batchNo);
        return stock;
    }
}