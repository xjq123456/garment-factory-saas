package com.garment.search.domain.query.repository;

import com.garment.search.domain.query.model.SearchQuery;
import com.garment.search.domain.query.model.SearchResult;

/**
 * 搜索查询仓库接口：负责全文搜索。
 */
public interface SearchQueryRepository {

    /**
     * 执行搜索查询。
     */
    SearchResult search(SearchQuery query);
}