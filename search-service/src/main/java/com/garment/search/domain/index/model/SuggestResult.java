package com.garment.search.domain.index.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 搜索建议结果模型。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuggestResult {

    /** 建议文本 */
    private String text;

    /** 来源索引类型 */
    private String indexType;

    /** 建议得分 */
    private float score;
}