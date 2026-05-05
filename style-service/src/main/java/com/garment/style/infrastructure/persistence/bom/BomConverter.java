package com.garment.style.infrastructure.persistence.bom;

import com.garment.style.domain.bom.entity.Bom;
import com.garment.style.domain.bom.entity.BomItem;

import java.util.ArrayList;
import java.util.List;

public final class BomConverter {

    private BomConverter() {}

    public static BomDO toDO(Bom domain) {
        if (domain == null) return null;
        BomDO d = new BomDO();
        d.setId(domain.getId());
        d.setTenantId(domain.getTenantId());
        d.setStyleId(domain.getStyleId());
        d.setBomCode(domain.getBomCode());
        d.setBomName(domain.getBomName());
        d.setVersionNo(domain.getVersionNo());
        d.setStatus(domain.getStatus());
        d.setRemark(domain.getRemark());
        return d;
    }

    public static Bom toDomain(BomDO d) {
        if (d == null) return null;
        Bom bom = Bom.create(d.getTenantId(), d.getStyleId(), d.getBomCode(),
                d.getBomName(), d.getVersionNo(), d.getRemark());
        bom.setId(d.getId());
        bom.overrideStatus(d.getStatus());
        return bom;
    }

    public static BomItemDO itemToDO(BomItem domain) {
        if (domain == null) return null;
        BomItemDO d = new BomItemDO();
        d.setId(domain.getId());
        d.setTenantId(domain.getTenantId());
        d.setBomId(domain.getBomId());
        d.setMaterialName(domain.getMaterialName());
        d.setMaterialCode(domain.getMaterialCode());
        d.setMaterialType(domain.getMaterialType());
        d.setSpecification(domain.getSpecification());
        d.setUnit(domain.getUnit());
        d.setQuantity(domain.getQuantity());
        d.setUnitPrice(domain.getUnitPrice());
        d.setSupplier(domain.getSupplier());
        d.setColor(domain.getColor());
        d.setRemark(domain.getRemark());
        d.setSortOrder(domain.getSortOrder());
        return d;
    }

    public static BomItem itemToDomain(BomItemDO d) {
        if (d == null) return null;
        BomItem item = BomItem.create(d.getTenantId(), d.getBomId(), d.getMaterialName(),
                d.getMaterialCode(), d.getMaterialType(), d.getSpecification(), d.getUnit(),
                d.getQuantity(), d.getUnitPrice(), d.getSupplier(), d.getColor(),
                d.getRemark(), d.getSortOrder());
        item.setId(d.getId());
        return item;
    }

    public static List<BomItem> itemsToDomain(List<BomItemDO> items) {
        if (items == null) return new ArrayList<>();
        return items.stream().map(BomConverter::itemToDomain).toList();
    }
}
