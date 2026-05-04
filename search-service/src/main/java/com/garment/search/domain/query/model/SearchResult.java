package com.garment.search.domain.query.model;

import com.garment.search.domain.index.model.SearchDocument;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 搜索结果：包含匹配文档列表及分页信息。
 */
@Data
@Builder
public class SearchResult {

    /** 匹配的文档列表 */
    private List<SearchDocument> documents;

    /** 总命中数 */
    private long totalHits;

    /** 当前页码（从 0 开始） */
    private int page;

    /** 每页大小 */
    private int size;

    /** 总页数 */
    private int totalPages;
}