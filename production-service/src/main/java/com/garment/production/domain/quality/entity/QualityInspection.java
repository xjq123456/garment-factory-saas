package com.garment.production.domain.quality.entity;

import com.garment.common.domain.AggregateRoot;
import com.garment.production.domain.quality.vo.InspectionResult;
import com.garment.production.domain.quality.vo.InspectionType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 质检记录 - 聚合根
 *
 * 记录生产过程中的质量检验信息。
 * 支持巡检、终检、来料检等多种质检类型。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class QualityInspection extends AggregateRoot {

    /** 质检编号 */
    private String inspectionNo;

    /** 生产工单ID */
    private Long orderId;

    /** 生产任务ID */
    private Long taskId;

    /** 质检类型 */
    private InspectionType inspectionType;

    /** 检验数量 */
    private Integer inspectQty;

    /** 合格数量 */
    private Integer passQty;

    /** 不合格数量 */
    private Integer rejectQty;

    /** 缺陷类型(JSON数组) */
    private String defectTypes;

    /** 缺陷描述 */
    private String defectDesc;

    /** 质检结果 */
    private InspectionResult result;

    /** 质检员ID */
    private Long inspectorId;

    /** 质检员姓名 */
    private String inspectorName;

    /** 质检时间 */
    private LocalDateTime inspectionTime;

    /** 备注 */
    private String remark;

    /**
     * 创建质检记录
     */
    public static QualityInspection create(String inspectionNo, Long orderId,
                                           InspectionType inspectionType, Integer inspectQty) {
        QualityInspection inspection = new QualityInspection();
        inspection.setInspectionNo(inspectionNo);
        inspection.setOrderId(orderId);
        inspection.setInspectionType(inspectionType);
        inspection.setInspectQty(inspectQty);
        inspection.setPassQty(0);
        inspection.setRejectQty(0);
        inspection.setResult(InspectionResult.PENDING);
        return inspection;
    }

    /**
     * 提交质检结果
     */
    public void submitResult(int passQty, int rejectQty, InspectionResult result,
                             Long inspectorId, String inspectorName) {
        if (this.result != InspectionResult.PENDING) {
            throw new IllegalStateException("该质检记录已提交过结果");
        }
        if (passQty + rejectQty != this.inspectQty) {
            throw new IllegalArgumentException("合格数与不合格数之和必须等于检验数量");
        }
        this.passQty = passQty;
        this.rejectQty = rejectQty;
        this.result = result;
        this.inspectorId = inspectorId;
        this.inspectorName = inspectorName;
        this.inspectionTime = LocalDateTime.now();
    }

    /**
     * 记录缺陷信息
     */
    public void recordDefects(String defectTypes, String defectDesc) {
        this.defectTypes = defectTypes;
        this.defectDesc = defectDesc;
    }

    /**
     * 获取合格率(百分比)
     */
    public double getPassRate() {
        if (inspectQty == null || inspectQty == 0) return 0;
        return (passQty * 100.0) / inspectQty;
    }

    /**
     * 是否合格（合格率>=95%为合格）
     */
    public boolean isQualified() {
        return getPassRate() >= 95.0;
    }
}