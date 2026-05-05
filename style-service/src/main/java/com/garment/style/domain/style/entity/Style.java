package com.garment.style.domain.style.entity;

import com.garment.common.domain.AggregateRoot;
import com.garment.common.domain.DomainEvent;
import com.garment.style.domain.style.event.StyleCreatedEvent;
import com.garment.style.domain.style.event.StyleStatusChangedEvent;
import com.garment.style.domain.style.event.StyleUpdatedEvent;
import lombok.Getter;

@Getter
public class Style extends AggregateRoot {

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

    private Style() {}

    /**
     * 创建款式（工厂方法）。
     * <p>
     * 注意：此方法不产生领域事件，由应用层负责创建并发布 {@link StyleCreatedEvent}。
     */
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
        return s;
    }

    public DomainEvent update(String styleName, Long categoryId, String season, String year,
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
        return new StyleUpdatedEvent(this.tenantId, this.id, this.styleCode);
    }

    public DomainEvent publish() {
        this.status = 1;
        return new StyleStatusChangedEvent(this.tenantId, this.id, this.styleCode, 1);
    }

    public DomainEvent deactivate() {
        this.status = 2;
        return new StyleStatusChangedEvent(this.tenantId, this.id, this.styleCode, 2);
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** 仅供基础设施层从数据库还原时使用，业务代码不应调用 */
    public void overrideStatus(Integer status) {
        this.status = status;
    }

    /** 仅供基础设施层从数据库还原时使用，业务代码不应调用 */
    public void overrideVersion(Integer version) {
        this.version = version;
    }
}
