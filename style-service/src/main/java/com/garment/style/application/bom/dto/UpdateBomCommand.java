package com.garment.style.application.bom.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateBomCommand {
    private String bomName;
    private String versionNo;
    private String remark;
    private List<CreateBomCommand.BomItemDTO> items;
}
