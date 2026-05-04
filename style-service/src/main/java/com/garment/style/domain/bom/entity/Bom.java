package com.garment.style.domain.bom.entity;

import com.garment.common.domain.AggregateRoot;
import com.garment.style.domain.bom.event.BomCreatedEvent;
import com.garment.style.domain.bom.event.BomStatusChangedEvent;
import com.garment.style.domain.bom.event.BomUpdatedEvent;
import com.garment.style.domain.bom.vo.BomStatus;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Bom extends AggregateRoot {

    private Long id;
    private Long tenantId;
    private Long styleId;
    private String bomCode;
    private String bomName;
    private String versionNo;
    private BomStatus status;
    private String remark;
    private List<BomItem> items = new ArrayList<>();

    private Bom() {}

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
        b.registerEvent(new BomCreatedEvent(tenantId, b.id, bomCode));
        return b;
    }

    public void update(String bomName, String versionNo, String remark) {
        this.bomName = bomName;
        this.versionNo = versionNo;
        this.remark = remark;
        this.registerEvent(new BomUpdatedEvent(this.tenantId, this.id, this.bomCode));
    }

    public void confirm() {
        this.status = BomStatus.CONFIRMED;
        this.registerEvent(new BomStatusChangedEvent(this.tenantId, this.id, this.bomCode, BomStatus.CONFIRMED.getCode()));
    }

    public void deprecate() {
        this.status = BomStatus.DEPRECATED;
        this.registerEvent(new BomStatusChangedEvent(this.tenantId, this.id, this.bomCode, BomStatus.DEPRECATED.getCode()));
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setItems(List<BomItem> items) {
        this.items = items != null ? items : new ArrayList<>();
    }
}
