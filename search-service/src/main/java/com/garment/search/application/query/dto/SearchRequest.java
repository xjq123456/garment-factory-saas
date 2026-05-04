package com.garment.search.application.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 统一搜索请求 DTO。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    /** 搜索关键词 */
    private String keyword;

    /** 索引类型过滤列表（可选，不传则搜索全部） */
    private List<String> indexTypes;

    /** 过滤条件 */
    private Map<String, Object> filters;

    /** 排序字段 */
    private String sortField;

    /** 是否降序 */
    private boolean sortDesc;

    /** 页码（从0开始） */
    @Builder.Default
    private int page = 0;

    /** 每页大小 */
    @Builder.Default
    private int size = 20;
}