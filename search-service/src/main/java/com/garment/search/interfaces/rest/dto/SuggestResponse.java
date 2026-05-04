package com.garment.search.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 搜索建议响应 DTO。
 */
@Data
public class SuggestResponse {

    private List<Suggestion> suggestions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Suggestion {
        private String id;
        private String title;
        private String type;
    }
}