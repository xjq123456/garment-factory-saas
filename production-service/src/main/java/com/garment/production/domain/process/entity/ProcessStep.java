package com.garment.production.domain.process.entity;

import com.garment.common.domain.BaseDomainEntity;
import com.garment.production.domain.workstation.vo.WorkstationType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 工序步骤 - 实体
 *
 * 工艺路线中的单个工序，如裁剪、缝制、熨烫、包装等。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ProcessStep extends BaseDomainEntity {

    /** 主键ID */
    private Long id;

    /** 所属工艺路线ID */
    private Long routeId;

    /** 工序序号 */
    private Integer stepNo;

    /** 工序名称 */
    private String stepName;

    /** 工序类型 */
    private WorkstationType stepType;

    /** 标准工时(分钟) */
    private BigDecimal standardTime;

    /** 标准产量(件/小时) */
    private Integer standardOutput;

    /** 工序说明 */
    private String description;

    /**
     * 创建工序步骤
     */
    public static ProcessStep create(String stepName, WorkstationType stepType) {
        ProcessStep step = new ProcessStep();
        step.setStepName(stepName);
        step.setStepType(stepType);
        return step;
    }

    /**
     * 创建工序步骤（含标准工时）
     */
    public static ProcessStep create(String stepName, WorkstationType stepType,
                                     BigDecimal standardTime, Integer standardOutput) {
        ProcessStep step = create(stepName, stepType);
        step.setStandardTime(standardTime);
        step.setStandardOutput(standardOutput);
        return step;
    }
}