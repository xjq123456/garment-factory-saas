package com.garment.search.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 高级搜索请求 DTO。
 */
@Data
public class SearchRequest {

    @NotBlank(message = "搜索关键词不能为空")
    private String keyword;

    /**
     * 搜索类型列表，如 ["STYLE", "ORDER", "PRODUCTION"]
     */
    private List<String> types;

    /**
     * 自定义过滤条件，如 {"status": "ACTIVE", "category": "衬衫"}
     */
    private Map<String, Object> filters;

    private Integer page = 0;
    private Integer size = 20;

    private String sortField;
    private boolean sortDesc = false;
}