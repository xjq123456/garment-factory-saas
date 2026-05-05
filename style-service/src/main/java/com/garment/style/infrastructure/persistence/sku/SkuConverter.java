package com.garment.style.infrastructure.persistence.sku;

import com.garment.style.domain.sku.entity.Sku;

public final class SkuConverter {

    private SkuConverter() {}

    public static SkuDO toDO(Sku domain) {
        if (domain == null) return null;
        SkuDO d = new SkuDO();
        d.setId(domain.getId());
        d.setTenantId(domain.getTenantId());
        d.setStyleId(domain.getStyleId());
        d.setSkuCode(domain.getSkuCode());
        d.setColor(domain.getColor());
        d.setColorCode(domain.getColorCode());
        d.setSize(domain.getSize());
        d.setSizeType(domain.getSizeType());
        d.setBarcode(domain.getBarcode());
        d.setWeight(domain.getWeight());
        d.setExtraPrice(domain.getExtraPrice());
        d.setStatus(domain.getStatus());
        d.setSortOrder(domain.getSortOrder());
        /*d.setVersion(domain.getVersion());*/
        return d;
    }

    public static Sku toDomain(SkuDO d) {
        if (d == null) return null;
        Sku sku = Sku.create(
                d.getTenantId(), d.getStyleId(), d.getSkuCode(),
                d.getColor(), d.getColorCode(), d.getSize(), d.getSizeType(),
                d.getBarcode(), d.getWeight(), d.getExtraPrice()
        );
        sku.setId(d.getId());
        try {
            java.lang.reflect.Field statusField = Sku.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(sku, d.getStatus());
            java.lang.reflect.Field sortField = Sku.class.getDeclaredField("sortOrder");
            sortField.setAccessible(true);
            sortField.set(sku, d.getSortOrder());
            java.lang.reflect.Field versionField = Sku.class.getDeclaredField("version");
            versionField.setAccessible(true);
            versionField.set(sku, d.getVersion());
        } catch (Exception e) {
            // ignore
        }
        return sku;
    }
}
