package com.garment.style.infrastructure.persistence.style;

import com.garment.style.domain.style.entity.Style;

public final class StyleConverter {

    private StyleConverter() {}

    public static StyleDO toDO(Style domain) {
        if (domain == null) return null;
        StyleDO d = new StyleDO();
        d.setId(domain.getId());
        d.setTenantId(domain.getTenantId());
        d.setStyleCode(domain.getStyleCode());
        d.setStyleName(domain.getStyleName());
        d.setCategoryId(domain.getCategoryId());
        d.setSeason(domain.getSeason());
        d.setYear(domain.getYear());
        d.setPatternType(domain.getPatternType());
        d.setCraftDesc(domain.getCraftDesc());
        d.setDesignSketch(domain.getDesignSketch());
        d.setMainImage(domain.getMainImage());
        d.setImages(domain.getImages());
        d.setTags(domain.getTags());
        if (domain.getStatus() != null) {
            d.setStatus(com.garment.style.domain.style.vo.StyleStatus.values()[domain.getStatus()]);
        }
        d.setRemark(domain.getRemark());
        d.setVersion(domain.getVersion());
        return d;
    }

    public static Style toDomain(StyleDO d) {
        if (d == null) return null;
        Style style = Style.create(
                d.getTenantId(), d.getStyleCode(), d.getStyleName(),
                d.getCategoryId(), d.getSeason(), d.getYear(),
                d.getPatternType(), d.getCraftDesc(), d.getDesignSketch(),
                d.getMainImage(), d.getImages(), d.getTags(), d.getRemark()
        );
        style.setId(d.getId());
        style.overrideStatus(d.getStatus() != null ? d.getStatus().getCode() : 0);
        style.overrideVersion(d.getVersion());
        return style;
    }
}
