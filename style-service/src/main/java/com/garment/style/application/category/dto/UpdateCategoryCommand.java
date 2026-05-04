package com.garment.style.application.category.dto;

import lombok.Data;

@Data
public class UpdateCategoryCommand {
    private String name;
    private Long parentId;
    private Integer sortOrder;
    private String icon;
}
