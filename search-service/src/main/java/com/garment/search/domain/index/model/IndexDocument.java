package com.garment.search.domain.index.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 索引文档模型。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexDocument {
    private String id;
    private IndexType indexType;
    private String title;
    private String body;
    private Map<String, Object> attributes;
}