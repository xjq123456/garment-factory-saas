package com.garment.search.application.query;

import com.garment.common.domain.TenantContext;
import com.garment.search.application.query.dto.SearchRequest;
import com.garment.search.domain.index.model.SearchResult;
import com.garment.search.domain.index.model.SuggestResult;
import com.garment.search.domain.index.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 搜索查询应用服务：处理搜索查询请求。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchQueryAppService {

    private final SearchRepository searchRepository;

    /**
     * 统一搜索。
     */
    public SearchResult search(SearchRequest request) {
        Long tenantId = TenantContext.getTenantId();
        log.info("搜索请求: keyword={}, indexTypes={}, tenantId={}", 
                request.getKeyword(), request.getIndexTypes(), tenantId);

        return searchRepository.search(
                request.getKeyword(),
                request.getIndexTypes(),
                request.getFilters(),
                request.getSortField(),
                request.isSortDesc(),
                request.getPage(),
                request.getSize(),
                tenantId
        );
    }

    /**
     * 搜索建议（自动补全）。
     */
    public List<SuggestResult> suggest(String keyword, String indexType) {
        Long tenantId = TenantContext.getTenantId();
        log.info("搜索建议: keyword={}, indexType={}, tenantId={}", keyword, indexType, tenantId);
        return searchRepository.suggest(keyword, indexType, tenantId);
    }
}