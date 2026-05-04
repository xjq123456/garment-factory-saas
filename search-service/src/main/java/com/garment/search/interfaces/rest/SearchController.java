package com.garment.search.interfaces.rest;

import com.garment.common.interfaces.R;
import com.garment.search.application.query.SearchAppService;
import com.garment.search.domain.index.model.IndexType;
import com.garment.search.domain.query.model.SearchQuery;
import com.garment.search.domain.query.model.SearchResult;
import com.garment.search.interfaces.rest.dto.SearchRequest;
import com.garment.search.interfaces.rest.dto.SearchResponse;
import com.garment.search.interfaces.rest.dto.SuggestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 搜索 REST 控制器 — 提供全文搜索和搜索建议 API。
 */
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchAppService searchAppService;

    /**
     * 全局搜索接口。
     *
     * @param keyword   搜索关键词
     * @param type      可选，限定搜索类型（STYLE, ORDER, PRODUCTION 等）
     * @param page      页码（从 0 开始）
     * @param size      每页数量
     * @param sortField 排序字段
     * @param sortDesc  是否降序
     * @return 分页搜索结果
     */
    @GetMapping
    public R<SearchResponse> search(
            @RequestParam String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sortField,
            @RequestParam(defaultValue = "false") boolean sortDesc) {

        List<IndexType> indexTypes = null;
        if (type != null && !type.isBlank()) {
            indexTypes = List.of(IndexType.valueOf(type.toUpperCase()));
        }

        SearchQuery query = SearchQuery.builder()
                .keyword(keyword)
                .indexTypes(indexTypes)
                .page(page)
                .size(size)
                .sortField(sortField)
                .sortDesc(sortDesc)
                .build();

        SearchResult result = searchAppService.search(query);
        return R.ok(toResponse(result));
    }

    /**
     * 带完整筛选条件的高级搜索接口。
     */
    @PostMapping("/advanced")
    public R<SearchResponse> advancedSearch(@Valid @RequestBody SearchRequest request) {
        List<IndexType> indexTypes = null;
        if (request.getTypes() != null && !request.getTypes().isEmpty()) {
            indexTypes = request.getTypes().stream()
                    .map(t -> IndexType.valueOf(t.toUpperCase()))
                    .collect(Collectors.toList());
        }

        SearchQuery query = SearchQuery.builder()
                .keyword(request.getKeyword())
                .indexTypes(indexTypes)
                .filters(request.getFilters())
                .page(request.getPage() != null ? request.getPage() : 0)
                .size(request.getSize() != null ? request.getSize() : 20)
                .sortField(request.getSortField())
                .sortDesc(request.isSortDesc())
                .build();

        SearchResult result = searchAppService.search(query);
        return R.ok(toResponse(result));
    }

    /**
     * 搜索建议 / 自动补全。
     */
    @GetMapping("/suggest")
    public R<SuggestResponse> suggest(
            @RequestParam String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "10") int size) {

        List<IndexType> indexTypes = null;
        if (type != null && !type.isBlank()) {
            indexTypes = List.of(IndexType.valueOf(type.toUpperCase()));
        }

        SearchQuery query = SearchQuery.builder()
                .keyword(keyword)
                .indexTypes(indexTypes)
                .page(0)
                .size(size)
                .build();

        SearchResult result = searchAppService.search(query);
        SuggestResponse response = new SuggestResponse();
        response.setSuggestions(result.getDocuments().stream()
                .map(doc -> new SuggestResponse.Suggestion(doc.getId(), doc.getTitle(), doc.getIndexType().name()))
                .collect(Collectors.toList()));
        return R.ok(response);
    }

    /**
     * 构建 SearchResponse。
     */
    private SearchResponse toResponse(SearchResult result) {
        SearchResponse response = new SearchResponse();
        response.setTotal(result.getTotalHits());
        response.setPage(result.getPage());
        response.setSize(result.getSize());
        response.setTotalPages(result.getTotalPages());

        response.setItems(result.getDocuments().stream()
                .map(doc -> {
                    SearchResponse.SearchItem item = new SearchResponse.SearchItem();
                    item.setId(doc.getId());
                    item.setType(doc.getIndexType() != null ? doc.getIndexType().name() : null);
                    item.setTitle(doc.getTitle());
                    item.setBody(doc.getBody());
                    item.setScore(doc.getScore());
                    item.setAttributes(doc.getAttributes());
                    item.setCreatedAt(doc.getCreatedAt());
                    item.setUpdatedAt(doc.getUpdatedAt());
                    return item;
                })
                .collect(Collectors.toList()));

        return response;
    }
}