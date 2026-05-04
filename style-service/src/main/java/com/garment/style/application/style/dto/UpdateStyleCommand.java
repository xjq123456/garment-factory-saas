package com.garment.style.application.style.dto;

import lombok.Data;

@Data
public class UpdateStyleCommand {
    private String styleName;
    private Long categoryId;
    private String season;
    private String year;
    private String patternType;
    private String craftDesc;
    private String designSketch;
    private String mainImage;
    private String images;
    private String tags;
    private String remark;
}
