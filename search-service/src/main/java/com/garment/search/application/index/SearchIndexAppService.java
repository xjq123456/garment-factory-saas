package com.garment.search.application.index;

import com.garment.common.domain.TenantContext;
import com.garment.search.domain.index.model.IndexType;
import com.garment.search.domain.index.model.SearchDocument;
import com.garment.search.domain.index.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 索引管理应用服务：处理索引的创建、更新、删除。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchIndexAppService {

    private final SearchRepository searchRepository;

    /**
     * 索引单个文档。
     */
    public void indexDocument(String id, String indexType, String title,
                              String body, Map<String, Object> attributes) {
        Long tenantId = TenantContext.getTenantId();
        SearchDocument document = SearchDocument.builder()
                .id(id)
                .indexType(IndexType.fromCode(indexType))
                .tenantId(tenantId)
                .title(title)
                .body(body)
                .attributes(attributes)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        searchRepository.index(document);
        log.info("索引文档: id={}, indexType={}, tenantId={}", id, indexType, tenantId);
    }

    /**
     * 批量索引文档。
     */
    public void bulkIndex(List<SearchDocument> documents) {
        searchRepository.bulkIndex(documents);
        log.info("批量索引文档: count={}", documents.size());
    }

    /**
     * 删除索引文档。
     */
    public void deleteDocument(String indexType, String id) {
        searchRepository.delete(indexType, id);
        log.info("删除索引文档: indexType={}, id={}", indexType, id);
    }
}