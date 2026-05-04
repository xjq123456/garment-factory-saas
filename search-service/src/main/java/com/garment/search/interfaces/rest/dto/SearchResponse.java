package com.garment.search.interfaces.rest.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 搜索响应 DTO。
 */
@Data
public class SearchResponse {

    private long total;
    private int page;
    private int size;
    private int totalPages;
    private List<SearchItem> items;

    /**
     * 搜索结果项。
     */
    @Data
    public static class SearchItem {
        private String id;
        private String type;
        private String title;
        private String body;
        private Float score;
        private Map<String, Object> attributes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}