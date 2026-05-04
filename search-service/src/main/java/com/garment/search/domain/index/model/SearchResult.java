package com.garment.search.domain.index.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 搜索结果领域模型。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult {

    /** 匹配文档列表 */
    private List<SearchDocument> documents;

    /** 总匹配数 */
    private long total;

    /** 当前页码 */
    private int page;

    /** 每页大小 */
    private int size;

    /** 搜索耗时（毫秒） */
    private long tookMs;
}