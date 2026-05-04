package com.garment.search.application.dto;

import com.garment.search.domain.index.model.IndexType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * 索引文档命令。
 */
@Data
public class IndexDocumentCommand {

    @NotBlank(message = "文档 ID 不能为空")
    private String id;

    @NotNull(message = "索引类型不能为空")
    private IndexType indexType;

    @NotNull(message = "租户 ID 不能为空")
    private Long tenantId;

    @NotBlank(message = "标题不能为空")
    private String title;

    private String body;

    private Map<String, Object> attributes;
}