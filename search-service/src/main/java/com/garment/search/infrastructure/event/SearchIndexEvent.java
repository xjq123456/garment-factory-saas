package com.garment.search.infrastructure.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 搜索索引事件：用于跨服务数据同步到 ES。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchIndexEvent {

    private String operation; // INDEX, UPDATE, DELETE
    private String indexType;
    private String documentId;
    private Long tenantId;
    private String title;
    private String body;
    private Map<String, Object> attributes;
    private LocalDateTime timestamp;
}