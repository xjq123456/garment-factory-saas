package com.garment.search.application.index;

import com.garment.search.domain.index.model.IndexDocument;
import com.garment.search.domain.index.model.IndexEvent;
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

    /**
     * 处理索引事件：根据事件类型路由到索引或删除操作。
     */
    public void handleEvent(IndexEvent event) {
        if (event == null || event.getEventType() == null) {
            log.warn("收到空索引事件，忽略");
            return;
        }

        boolean isDelete = event.getEventType().contains("DELETED") || event.getEventType().contains("deleted");

        if (isDelete) {
            searchIndexAppService.deleteDocument(event.getIndexType(), event.getDocumentId());
            log.debug("事件触发删除索引: id={}, indexType={}", event.getDocumentId(), event.getIndexType());
        } else {
            IndexType type = null;
            try {
                type = IndexType.fromCode(event.getIndexType());
            } catch (IllegalArgumentException ignored) {
            }
            IndexDocument doc = IndexDocument.builder()
                    .id(event.getDocumentId())
                    .indexType(type)
                    .title(event.getTitle())
                    .body(event.getBody())
                    .build();
            indexDocument(doc);
            log.debug("事件触发索引文档: id={}, indexType={}", event.getDocumentId(), event.getIndexType());
        }
    }
}