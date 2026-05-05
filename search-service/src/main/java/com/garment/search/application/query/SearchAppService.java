package com.garment.search.application.query;

import com.garment.search.domain.query.model.SearchQuery;
import com.garment.search.domain.query.model.SearchResult;
import com.garment.search.domain.query.repository.SearchQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 搜索应用服务（查询侧）：处理搜索请求，委托给查询仓储执行。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchAppService {

    private final SearchQueryRepository searchQueryRepository;

    /**
     * 全文搜索。
     */
    public SearchResult search(SearchQuery query) {
        log.info("执行搜索: keyword={}, page={}, size={}", query.getKeyword(), query.getPage(), query.getSize());
        return searchQueryRepository.search(query);
    }
}