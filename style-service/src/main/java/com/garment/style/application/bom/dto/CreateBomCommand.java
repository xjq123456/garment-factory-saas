package com.garment.style.application.bom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateBomCommand {
    @NotNull(message = "款式ID不能为空")
    private Long styleId;
    @NotBlank(message = "BOM编码不能为空")
    private String bomCode;
    @NotBlank(message = "BOM名称不能为空")
    private String bomName;
    private String versionNo;
    private String remark;
    private List<BomItemDTO> items;

    @Data
    public static class BomItemDTO {
        @NotBlank(message = "物料名称不能为空")
        private String materialName;
        private String materialCode;
        private String materialType;
        private String specification;
        private String unit;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private String supplier;
        private String color;
        private String remark;
        private Integer sortOrder;
    }
}
