package com.garment.search.application.index;

import com.garment.search.domain.index.model.IndexDocument;
import com.garment.search.domain.index.model.IndexType;
import com.garment.search.domain.index.model.SearchDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 索引应用服务：处理索引文档的增删操作。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IndexAppService {

    private final SearchIndexAppService searchIndexAppService;

    /**
     * 索引文档。
     */
    public void indexDocument(IndexDocument document) {
        searchIndexAppService.indexDocument(
                document.getId(),
                document.getIndexType() != null ? document.getIndexType().name() : "GENERAL",
                document.getTitle(),
                document.getBody(),
                document.getAttributes() != null ? document.getAttributes() : Map.of()
        );
    }

    /**
     * 删除文档。
     */
    public void deleteDocument(String id, IndexType indexType) {
        searchIndexAppService.deleteDocument(
                indexType != null ? indexType.name() : "GENERAL",
                id
        );
    }
}