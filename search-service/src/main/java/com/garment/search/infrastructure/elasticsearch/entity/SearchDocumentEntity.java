package com.garment.search.infrastructure.elasticsearch.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Elasticsearch 文档实体：用于与 ES 交互的数据载体。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchDocumentEntity {

    @JsonProperty("id")
    private String id;

    @JsonProperty("index_type")
    private String indexType;

    @JsonProperty("tenant_id")
    private Long tenantId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String body;

    @JsonProperty("attributes")
    private Map<String, Object> attributes;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}