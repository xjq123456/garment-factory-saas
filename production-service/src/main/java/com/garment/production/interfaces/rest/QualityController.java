package com.garment.production.interfaces.rest;

import com.garment.common.interfaces.PageResult;
import com.garment.common.interfaces.R;
import com.garment.production.application.quality.QualityAppService;
import com.garment.production.application.quality.dto.CreateInspectionCommand;
import com.garment.production.application.quality.dto.QualityInspectionVO;
import com.garment.production.application.quality.dto.SubmitInspectionCommand;
import com.garment.production.domain.quality.vo.InspectionResult;
import com.garment.production.domain.quality.vo.InspectionType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 质检管理控制器
 */
@RestController
@RequestMapping("/api/production/quality")
@RequiredArgsConstructor
public class QualityController {

    private final QualityAppService qualityAppService;

    /**
     * 创建质检记录
     */
    @PostMapping("/inspections")
    public R<QualityInspectionVO> createInspection(@Valid @RequestBody CreateInspectionCommand cmd) {
        return R.ok(qualityAppService.createInspection(cmd));
    }

    /**
     * 提交质检结果
     */
    @PostMapping("/inspections/submit")
    public R<QualityInspectionVO> submitResult(@Valid @RequestBody SubmitInspectionCommand cmd) {
        return R.ok(qualityAppService.submitResult(cmd));
    }

    /**
     * 查询质检记录详情
     */
    @GetMapping("/inspections/{inspectionId}")
    public R<QualityInspectionVO> getInspection(@PathVariable Long inspectionId) {
        return R.ok(qualityAppService.getInspection(inspectionId));
    }

    /**
     * 按工单查询质检记录
     */
    @GetMapping("/inspections/order/{orderId}")
    public R<List<QualityInspectionVO>> listByOrder(@PathVariable Long orderId) {
        return R.ok(qualityAppService.listByOrder(orderId));
    }

    /**
     * 分页查询质检记录
     */
    @GetMapping("/inspections")
    public R<PageResult<QualityInspectionVO>> listInspections(
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) InspectionType type,
            @RequestParam(required = false) InspectionResult result,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return R.ok(qualityAppService.findInspections(orderId, type, result, page, size));
    }
}