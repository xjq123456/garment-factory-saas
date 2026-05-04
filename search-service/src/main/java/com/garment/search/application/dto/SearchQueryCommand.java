package com.garment.search.application.dto;

import com.garment.search.domain.index.model.IndexType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 搜索查询命令。
 */
@Data
public class SearchQueryCommand {

    @NotBlank(message = "搜索关键词不能为空")
    private String keyword;

    private List<IndexType> indexTypes;

    @NotNull(message = "租户 ID 不能为空")
    private Long tenantId;

    private Map<String, Object> filters;

    private String sortField;

    private boolean sortDesc;

    @Min(value = 0, message = "页码不能小于 0")
    private int page = 0;

    @Min(value = 1, message = "每页大小不能小于 1")
    private int size = 20;
}