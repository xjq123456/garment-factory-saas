package com.garment.search.domain.index.repository;

import com.garment.search.domain.index.model.SearchDocument;
import com.garment.search.domain.index.model.SearchResult;
import com.garment.search.domain.index.model.SuggestResult;

import java.util.List;
import java.util.Map;

/**
 * 搜索仓储接口：定义搜索索引的增删查操作。
 */
public interface SearchRepository {

    /**
     * 索引（新增/更新）文档。
     */
    void index(SearchDocument document);

    /**
     * 批量索引文档。
     */
    void bulkIndex(List<SearchDocument> documents);

    /**
     * 根据索引类型和ID删除文档。
     */
    void delete(String indexType, String id);

    /**
     * 统一全文搜索。
     *
     * @param keyword    搜索关键词
     * @param indexTypes 索引类型列表（为空则搜索全部）
     * @param filters    过滤条件
     * @param sortField  排序字段
     * @param sortDesc   是否降序
     * @param page       页码（从0开始）
     * @param size       每页大小
     * @return 搜索结果
     */
    SearchResult search(String keyword, List<String> indexTypes,
                        Map<String, Object> filters, String sortField,
                        boolean sortDesc, int page, int size);

    /**
     * 搜索建议（自动补全）。
     */
    List<SuggestResult> suggest(String keyword, String indexType);
}