package com.garment.search.domain.query.model;

import com.garment.search.domain.index.model.IndexType;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 搜索查询：封装搜索请求的参数。
 */
@Data
@Builder
public class SearchQuery {

    /** 搜索关键词 */
    private String keyword;

    /** 限定搜索的索引类型，为空则搜索全部 */
    private List<IndexType> indexTypes;

    /** 租户 ID */
    private Long tenantId;

    /** 过滤条件：key 为字段名，value 为过滤值 */
    private Map<String, Object> filters;

    /** 排序字段 */
    private String sortField;

    /** 是否降序 */
    private boolean sortDesc;

    /** 页码（从 0 开始） */
    private int page;

    /** 每页大小 */
    private int size;
}