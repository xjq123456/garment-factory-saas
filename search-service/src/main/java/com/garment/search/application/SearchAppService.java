package com.garment.search.application;

import com.garment.search.application.dto.IndexDocumentCommand;
import com.garment.search.application.dto.SearchQueryCommand;
import com.garment.search.domain.index.model.SearchDocument;
import com.garment.search.domain.index.repository.SearchIndexRepository;
import com.garment.search.domain.query.model.SearchQuery;
import com.garment.search.domain.query.model.SearchResult;
import com.garment.search.domain.query.repository.SearchQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 搜索应用服务：编排索引和查询操作。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchAppService {

    private final SearchIndexRepository searchIndexRepository;
    private final SearchQueryRepository searchQueryRepository;

    /**
     * 索引单个文档。
     */
    public void indexDocument(IndexDocumentCommand command) {
        SearchDocument document = SearchDocument.builder()
                .id(command.getId())
                .indexType(command.getIndexType())
                .tenantId(command.getTenantId())
                .title(command.getTitle())
                .body(command.getBody())
                .attributes(command.getAttributes())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        log.info("索引文档: type={}, id={}, tenantId={}", document.getIndexType(), document.getId(), document.getTenantId());
        searchIndexRepository.index(document);
    }

    /**
     * 批量索引文档。
     */
    public void bulkIndex(List<IndexDocumentCommand> commands) {
        List<SearchDocument> documents = commands.stream()
                .map(cmd -> SearchDocument.builder()
                        .id(cmd.getId())
                        .indexType(cmd.getIndexType())
                        .tenantId(cmd.getTenantId())
                        .title(cmd.getTitle())
                        .body(cmd.getBody())
                        .attributes(cmd.getAttributes())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build())
                .toList();

        log.info("批量索引文档: count={}, tenantId={}", documents.size(), documents.get(0).getTenantId());
        searchIndexRepository.bulkIndex(documents);
    }

    /**
     * 删除索引文档。
     */
    public void deleteDocument(String indexType, String id) {
        log.info("删除文档: indexType={}, id={}", indexType, id);
        searchIndexRepository.delete(indexType, id);
    }

    /**
     * 全文搜索。
     */
    public SearchResult search(SearchQueryCommand command) {
        SearchQuery query = SearchQuery.builder()
                .keyword(command.getKeyword())
                .indexTypes(command.getIndexTypes())
                .tenantId(command.getTenantId())
                .filters(command.getFilters())
                .sortField(command.getSortField())
                .sortDesc(command.isSortDesc())
                .page(command.getPage())
                .size(command.getSize())
                .build();

        log.info("执行搜索: keyword={}, tenantId={}, page={}, size={}", query.getKeyword(), query.getTenantId(), query.getPage(), query.getSize());
        return searchQueryRepository.search(query);
    }
}