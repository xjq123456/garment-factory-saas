package com.garment.search.application.query;

import com.garment.common.domain.AuthUserContext;
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
        log.info("搜索请求: keyword={}, indexTypes={}",
                request.getKeyword(), request.getIndexTypes());

        return searchRepository.search(
                request.getKeyword(),
                request.getIndexTypes(),
                request.getFilters(),
                request.getSortField(),
                request.isSortDesc(),
                request.getPage(),
                request.getSize()
        );
    }

    /**
     * 搜索建议（自动补全）。
     */
    public List<SuggestResult> suggest(String keyword, String indexType) {
        log.info("搜索建议: keyword={}, indexType={}", keyword, indexType);
        return searchRepository.suggest(keyword, indexType);
    }
}