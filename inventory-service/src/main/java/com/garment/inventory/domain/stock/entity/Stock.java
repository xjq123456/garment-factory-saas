package com.garment.inventory.domain.stock.entity;

import com.garment.common.domain.AggregateRoot;
import com.garment.common.domain.BizException;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 成品库存实体 - 聚合根
 * 以SKU为维度管理库存
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Stock extends AggregateRoot {

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * 款号ID
     */
    private Long styleId;

    /**
     * 款号编码
     */
    private String styleCode;

    /**
     * 颜色
     */
    private String color;

    /**
     * 尺码
     */
    private String size;

    /**
     * 总库存数量
     */
    private Integer totalQty;

    /**
     * 可用库存数量
     */
    private Integer availableQty;

    /**
     * 锁定库存数量
     */
    private Integer lockedQty;

    /**
     * 安全库存
     */
    private Integer safetyStock;

    /**
     * 单位
     */
    private String unit;

    /**
     * 备注
     */
    private String remark;

    /**
     * 入库
     *
     * @param qty 入库数量
     */
    public void inbound(int qty) {
        if (qty <= 0) {
            throw new BizException("入库数量必须大于0");
        }
        this.totalQty += qty;
        this.availableQty += qty;
    }

    /**
     * 出库
     *
     * @param qty 出库数量
     */
    public void outbound(int qty) {
        if (qty <= 0) {
            throw new BizException("出库数量必须大于0");
        }
        if (this.availableQty < qty) {
            throw new BizException("可用库存不足, 当前可用: " + this.availableQty + ", 需要: " + qty);
        }
        this.totalQty -= qty;
        this.availableQty -= qty;
    }

    /**
     * 锁定库存(待出库)
     *
     * @param qty 锁定数量
     */
    public void lock(int qty) {
        if (qty <= 0) {
            throw new BizException("锁定数量必须大于0");
        }
        if (this.availableQty < qty) {
            throw new BizException("可用库存不足, 无法锁定");
        }
        this.availableQty -= qty;
        this.lockedQty += qty;
    }

    /**
     * 解锁库存
     *
     * @param qty 解锁数量
     */
    public void unlock(int qty) {
        if (qty <= 0) {
            throw new BizException("解锁数量必须大于0");
        }
        if (this.lockedQty < qty) {
            throw new BizException("锁定库存不足, 无法解锁");
        }
        this.availableQty += qty;
        this.lockedQty -= qty;
    }

    /**
     * 确认出库(从锁定状态扣减)
     *
     * @param qty 出库数量
     */
    public void confirmOutbound(int qty) {
        if (qty <= 0) {
            throw new BizException("出库数量必须大于0");
        }
        if (this.lockedQty < qty) {
            throw new BizException("锁定库存不足, 无法确认出库");
        }
        this.totalQty -= qty;
        this.lockedQty -= qty;
    }

    /**
     * 是否低于安全库存
     */
    public boolean isBelowSafetyStock() {
        return this.safetyStock != null && this.safetyStock > 0 && this.totalQty < this.safetyStock;
    }

    /**
     * 创建库存记录
     */
    public static Stock create(Long warehouseId, Long skuId, String styleCode, String color, String size) {
        Stock stock = new Stock();
        stock.setWarehouseId(warehouseId);
        stock.setSkuId(skuId);
        stock.setStyleCode(styleCode);
        stock.setColor(color);
        stock.setSize(size);
        stock.setTotalQty(0);
        stock.setAvailableQty(0);
        stock.setLockedQty(0);
        stock.setUnit("件");
        return stock;
    }
}