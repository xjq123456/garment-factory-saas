package com.garment.style.domain.style.entity;

import com.garment.common.domain.AggregateRoot;
import com.garment.style.domain.style.event.StyleCreatedEvent;
import com.garment.style.domain.style.event.StylePublishedEvent;
import com.garment.style.domain.style.event.StyleUpdatedEvent;
import com.garment.style.domain.style.event.StyleStatusChangedEvent;
import lombok.Getter;

@Getter
public class Style extends AggregateRoot {

    private Long id;
    private Long tenantId;
    private String styleCode;
    private String styleName;
    private Long categoryId;
    private String season;
    private String year;
    private String patternType;
    private String craftDesc;
    private String designSketch;
    private String mainImage;
    /** 多图URL，JSON数组 */
    private String images;
    private String tags;
    /** 0=草稿 1=已发布 2=已停用 */
    private Integer status;
    private String remark;
    private Integer version;

    private Style() {}

    public static Style create(Long tenantId, String styleCode, String styleName,
                               Long categoryId, String season, String year,
                               String patternType, String craftDesc,
                               String designSketch, String mainImage,
                               String images, String tags, String remark) {
        Style s = new Style();
        s.tenantId = tenantId;
        s.styleCode = styleCode;
        s.styleName = styleName;
        s.categoryId = categoryId;
        s.season = season;
        s.year = year;
        s.patternType = patternType;
        s.craftDesc = craftDesc;
        s.designSketch = designSketch;
        s.mainImage = mainImage;
        s.images = images;
        s.tags = tags;
        s.status = 0;
        s.remark = remark;
        s.version = 1;
        s.registerEvent(new StyleCreatedEvent(tenantId, styleCode, styleName));
        return s;
    }

    public void update(String styleName, Long categoryId, String season, String year,
                       String patternType, String craftDesc, String designSketch,
                       String mainImage, String images, String tags, String remark) {
        this.styleName = styleName;
        this.categoryId = categoryId;
        this.season = season;
        this.year = year;
        this.patternType = patternType;
        this.craftDesc = craftDesc;
        this.designSketch = designSketch;
        this.mainImage = mainImage;
        this.images = images;
        this.tags = tags;
        this.remark = remark;
        this.version++;
        this.registerEvent(new StyleUpdatedEvent(this.tenantId, this.id, this.styleCode));
    }

    public void publish() {
        this.status = 1;
        this.registerEvent(new StyleStatusChangedEvent(this.tenantId, this.id, this.styleCode, 1));
    }

    public void deactivate() {
        this.status = 2;
        this.registerEvent(new StyleStatusChangedEvent(this.tenantId, this.id, this.styleCode, 2));
    }

    public void setId(Long id) {
        this.id = id;
    }
}