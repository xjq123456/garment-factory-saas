package com.garment.style.interfaces.rest;

import com.garment.common.interfaces.PageResult;
import com.garment.common.interfaces.PageQuery;
import com.garment.common.interfaces.R;
import com.garment.style.application.bom.BomAppService;
import com.garment.style.application.bom.dto.CreateBomCommand;
import com.garment.style.application.bom.dto.UpdateBomCommand;
import com.garment.style.application.category.CategoryAppService;
import com.garment.style.application.category.dto.CreateCategoryCommand;
import com.garment.style.application.category.dto.UpdateCategoryCommand;
import com.garment.style.application.sku.SkuAppService;
import com.garment.style.application.sku.dto.CreateSkuCommand;
import com.garment.style.application.sku.dto.UpdateSkuCommand;
import com.garment.style.application.style.StyleAppService;
import com.garment.style.application.style.dto.CreateStyleCommand;
import com.garment.style.application.style.dto.UpdateStyleCommand;
import com.garment.style.domain.bom.entity.Bom;
import com.garment.style.domain.category.entity.Category;
import com.garment.style.domain.sku.entity.Sku;
import com.garment.style.domain.style.entity.Style;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 款式管理聚合 Controller。
 * <p>
 * 前缀 /v1/styles 统一管理款式、SKU、BOM、分类四类资源，
 * 经由 gateway-service StripPrefix=2 后匹配 style-service 内部路径。
 */
@RestController
@RequestMapping("/v1/styles")
@RequiredArgsConstructor
public class StyleController {

    private final StyleAppService styleAppService;
    private final SkuAppService skuAppService;
    private final BomAppService bomAppService;
    private final CategoryAppService categoryAppService;

    // ==================== 款式 ====================

    @PostMapping
    public R<Style> createStyle(@Valid @RequestBody CreateStyleCommand cmd) {
        return R.ok(styleAppService.createStyle(cmd));
    }

    @PutMapping("/{styleId}")
    public R<Style> updateStyle(@PathVariable Long styleId,
                                @Valid @RequestBody UpdateStyleCommand cmd) {
        return R.ok(styleAppService.updateStyle(styleId, cmd));
    }

    @DeleteMapping("/{styleId}")
    public R<Void> deleteStyle(@PathVariable Long styleId) {
        styleAppService.deleteStyle(styleId);
        return R.ok();
    }

    @GetMapping("/{styleId}")
    public R<Style> getStyle(@PathVariable Long styleId) {
        return R.ok(styleAppService.getStyle(styleId));
    }

    @GetMapping
    public R<PageResult<Style>> pageStyles(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String season,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(styleAppService.pageQuery(keyword, categoryId, season, status, pageNum, pageSize));
    }

    @PutMapping("/{styleId}/publish")
    public R<Style> publishStyle(@PathVariable Long styleId) {
        return R.ok(styleAppService.publishStyle(styleId));
    }

    @PutMapping("/{styleId}/deactivate")
    public R<Style> deactivateStyle(@PathVariable Long styleId) {
        return R.ok(styleAppService.deactivateStyle(styleId));
    }

    // ==================== SKU ====================

    @PostMapping("/skus")
    public R<Sku> createSku(@Valid @RequestBody CreateSkuCommand cmd) {
        return R.ok(skuAppService.createSku(cmd));
    }

    @PutMapping("/skus/{skuId}")
    public R<Sku> updateSku(@PathVariable Long skuId,
                             @Valid @RequestBody UpdateSkuCommand cmd) {
        return R.ok(skuAppService.updateSku(skuId, cmd));
    }

    @DeleteMapping("/skus/{skuId}")
    public R<Void> deleteSku(@PathVariable Long skuId) {
        skuAppService.deleteSku(skuId);
        return R.ok();
    }

    @GetMapping("/skus/{skuId}")
    public R<Sku> getSku(@PathVariable Long skuId) {
        return R.ok(skuAppService.getSku(skuId));
    }

    @GetMapping("/{styleId}/skus")
    public R<List<Sku>> listSkus(@PathVariable Long styleId) {
        return R.ok(skuAppService.listByStyleId(styleId));
    }

    @PutMapping("/skus/{skuId}/enable")
    public R<Void> enableSku(@PathVariable Long skuId) {
        skuAppService.enableSku(skuId);
        return R.ok();
    }

    @PutMapping("/skus/{skuId}/disable")
    public R<Void> disableSku(@PathVariable Long skuId) {
        skuAppService.disableSku(skuId);
        return R.ok();
    }

    // ==================== BOM ====================

    @PostMapping("/boms")
    public R<Bom> createBom(@Valid @RequestBody CreateBomCommand cmd) {
        return R.ok(bomAppService.createBom(cmd));
    }

    @PutMapping("/boms/{bomId}")
    public R<Bom> updateBom(@PathVariable Long bomId,
                             @Valid @RequestBody UpdateBomCommand cmd) {
        return R.ok(bomAppService.updateBom(bomId, cmd));
    }

    @DeleteMapping("/boms/{bomId}")
    public R<Void> deleteBom(@PathVariable Long bomId) {
        bomAppService.deleteBom(bomId);
        return R.ok();
    }

    @GetMapping("/boms/{bomId}")
    public R<Bom> getBom(@PathVariable Long bomId) {
        return R.ok(bomAppService.getBom(bomId));
    }

    @GetMapping("/{styleId}/boms")
    public R<List<Bom>> listBoms(@PathVariable Long styleId) {
        return R.ok(bomAppService.listByStyleId(styleId));
    }

    @PutMapping("/boms/{bomId}/confirm")
    public R<Bom> confirmBom(@PathVariable Long bomId) {
        return R.ok(bomAppService.confirmBom(bomId));
    }

    @PutMapping("/boms/{bomId}/deprecate")
    public R<Bom> deprecateBom(@PathVariable Long bomId) {
        return R.ok(bomAppService.deprecateBom(bomId));
    }

    // ==================== 分类 ====================

    @PostMapping("/categories")
    public R<Category> createCategory(@Valid @RequestBody CreateCategoryCommand cmd) {
        return R.ok(categoryAppService.createCategory(cmd));
    }

    @PutMapping("/categories/{categoryId}")
    public R<Category> updateCategory(@PathVariable Long categoryId,
                                       @Valid @RequestBody UpdateCategoryCommand cmd) {
        return R.ok(categoryAppService.updateCategory(categoryId, cmd));
    }

    @DeleteMapping("/categories/{categoryId}")
    public R<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryAppService.deleteCategory(categoryId);
        return R.ok();
    }

    @GetMapping("/categories/{categoryId}")
    public R<Category> getCategory(@PathVariable Long categoryId) {
        return R.ok(categoryAppService.getCategory(categoryId));
    }

    @GetMapping("/categories")
    public R<List<Category>> listCategories(
            @RequestParam(required = false) Long parentId) {
        if (parentId != null) {
            return R.ok(categoryAppService.listByParentId(parentId));
        }
        return R.ok(categoryAppService.listCategories());
    }

    @PutMapping("/categories/{categoryId}/enable")
    public R<Void> enableCategory(@PathVariable Long categoryId) {
        categoryAppService.enableCategory(categoryId);
        return R.ok();
    }

    @PutMapping("/categories/{categoryId}/disable")
    public R<Void> disableCategory(@PathVariable Long categoryId) {
        categoryAppService.disableCategory(categoryId);
        return R.ok();
    }
}
