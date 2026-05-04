package com.garment.inventory.domain.warehouse.entity;

import com.garment.common.domain.AggregateRoot;
import com.garment.inventory.domain.warehouse.vo.WarehouseStatus;
import com.garment.inventory.domain.warehouse.vo.WarehouseType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 仓库实体 - 聚合根
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Warehouse extends AggregateRoot {

    /**
     * 仓库编码
     */
    private String warehouseCode;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 仓库类型
     */
    private WarehouseType warehouseType;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 仓库地址
     */
    private String address;

    /**
     * 状态
     */
    private WarehouseStatus status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建仓库
     */
    public static Warehouse create(String code, String name, WarehouseType type) {
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseCode(code);
        warehouse.setWarehouseName(name);
        warehouse.setWarehouseType(type);
        warehouse.setStatus(WarehouseStatus.ENABLED);
        return warehouse;
    }

    /**
     * 启用仓库
     */
    public void enable() {
        this.status = WarehouseStatus.ENABLED;
    }

    /**
     * 禁用仓库
     */
    public void disable() {
        this.status = WarehouseStatus.DISABLED;
    }

    /**
     * 是否启用
     */
    public boolean isEnabled() {
        return this.status == WarehouseStatus.ENABLED;
    }
}