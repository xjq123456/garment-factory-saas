package com.garment.search.domain.index.repository;

import com.garment.search.domain.index.model.SearchDocument;

import java.util.List;

/**
 * 搜索索引仓库接口：负责文档的增删改。
 */
public interface SearchIndexRepository {

    /**
     * 索引单个文档。
     */
    void index(SearchDocument document);

    /**
     * 批量索引文档。
     */
    void bulkIndex(List<SearchDocument> documents);

    /**
     * 根据索引类型和业务 ID 删除文档。
     */
    void delete(String indexType, String id);

    /**
     * 根据索引类型和业务 ID 更新文档。
     */
    void update(SearchDocument document);
}