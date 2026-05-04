package com.garment.search.interfaces.rest;

import com.garment.common.interfaces.R;
import com.garment.search.application.index.IndexAppService;
import com.garment.search.domain.index.model.IndexDocument;
import com.garment.search.domain.index.model.IndexType;
import com.garment.search.infrastructure.index.IndexInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 索引管理 REST 控制器 — 提供手动索引管理（重建、文档增删）等运维 API。
 */
@RestController
@RequestMapping("/api/v1/search/index")
@RequiredArgsConstructor
public class IndexController {

    private final IndexAppService indexAppService;
    private final IndexInitializer indexInitializer;

    /**
     * 手动创建/重建索引。
     */
    @PostMapping("/create/{indexName}")
    public R<String> createIndex(@PathVariable String indexName) {
        indexInitializer.createIndex(indexName);
        return R.ok("索引创建成功: " + indexName);
    }

    /**
     * 检查索引是否存在。
     */
    @GetMapping("/exists/{indexName}")
    public R<Map<String, Boolean>> indexExists(@PathVariable String indexName) {
        boolean exists = indexInitializer.indexExists(indexName);
        return R.ok(Map.of("exists", exists));
    }

    /**
     * 手动索引文档（用于数据修复或手动同步）。
     */
    @PutMapping("/document")
    public R<String> indexDocument(@RequestBody IndexDocument document) {
        indexAppService.indexDocument(document);
        return R.ok("文档索引成功");
    }

    /**
     * 手动删除文档。
     */
    @DeleteMapping("/document/{id}")
    public R<String> deleteDocument(@PathVariable String id,
                                     @RequestParam IndexType indexType) {
        indexAppService.deleteDocument(id, indexType);
        return R.ok("文档删除成功");
    }

    /**
     * 触发全量重建索引（通过重放事件）。
     * 注意：实际生产环境中此操作应有权限控制和限流。
     */
    @PostMapping("/reindex/{indexType}")
    public R<String> reindex(@PathVariable String indexType) {
        // 重建指定类型的索引，此处仅创建索引结构
        String indexName = indexType.toLowerCase();
        indexInitializer.createIndex(indexName);
        return R.ok("索引结构已重建: " + indexName + "，请通过业务服务重新发布事件以填充数据");
    }
}