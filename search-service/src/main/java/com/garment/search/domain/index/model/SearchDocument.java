package com.garment.search.domain.index.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 搜索文档领域模型：统一的搜索索引文档。
 * <p>
 * 作为跨业务模块的统一搜索数据结构，支持款型、SKU、订单、库存等多种索引类型。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchDocument {

    /** 文档唯一标识（通常为业务ID） */
    private String id;

    /** 索引类型 */
    private IndexType indexType;

    /** 租户ID（多租户隔离） */
    private Long tenantId;

    /** 文档标题（用于全文检索主字段） */
    private String title;

    /** 文档正文内容（用于全文检索） */
    private String body;

    /** 扩展属性（不同索引类型的业务字段） */
    private Map<String, Object> attributes;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}