package com.garment.search.domain.index.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 索引事件模型。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexEvent {
    private String documentId;
    private String indexType;
    private String eventType;
    private String title;
    private String body;
    private String data;
}