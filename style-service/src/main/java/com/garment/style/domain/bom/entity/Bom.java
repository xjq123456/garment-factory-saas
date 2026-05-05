package com.garment.style.domain.bom.entity;

import com.garment.common.domain.AggregateRoot;
import com.garment.common.domain.DomainEvent;
import com.garment.style.domain.bom.event.BomCreatedEvent;
import com.garment.style.domain.bom.event.BomStatusChangedEvent;
import com.garment.style.domain.bom.event.BomUpdatedEvent;
import com.garment.style.domain.bom.vo.BomStatus;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Bom extends AggregateRoot {

    private Long styleId;
    private String bomCode;
    private String bomName;
    private String versionNo;
    private BomStatus status;
    private String remark;
    private List<BomItem> items = new ArrayList<>();

    private Bom() {}

    /**
     * 创建BOM（工厂方法）。
     * <p>
     * 注意：此方法不产生领域事件，由应用层负责创建并发布 {@link BomCreatedEvent}。
     */
    public static Bom create(Long tenantId, Long styleId, String bomCode,
                             String bomName, String versionNo, String remark) {
        Bom b = new Bom();
        b.tenantId = tenantId;
        b.styleId = styleId;
        b.bomCode = bomCode;
        b.bomName = bomName;
        b.versionNo = versionNo != null ? versionNo : "V1.0";
        b.status = BomStatus.DRAFT;
        b.remark = remark;
        return b;
    }

    public DomainEvent update(String bomName, String versionNo, String remark) {
        this.bomName = bomName;
        this.versionNo = versionNo;
        this.remark = remark;
        return new BomUpdatedEvent(this.tenantId, this.id, this.bomCode);
    }

    public DomainEvent confirm() {
        this.status = BomStatus.CONFIRMED;
        return new BomStatusChangedEvent(this.tenantId, this.id, this.bomCode, BomStatus.CONFIRMED.getCode());
    }

    public DomainEvent deprecate() {
        this.status = BomStatus.DEPRECATED;
        return new BomStatusChangedEvent(this.tenantId, this.id, this.bomCode, BomStatus.DEPRECATED.getCode());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setItems(List<BomItem> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

    /** 仅供基础设施层从数据库还原时使用，业务代码不应调用 */
    public void overrideStatus(BomStatus status) {
        this.status = status;
    }
}
