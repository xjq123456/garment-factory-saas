package com.garment.style.application.style.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateStyleCommand {
    private String styleCode;
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
    private List<SkuDTO> skus;

    @Data
    public static class SkuDTO {
        private String color;
        private String colorCode;
        private List<String> sizes;
        private String sizeType;
    }
}