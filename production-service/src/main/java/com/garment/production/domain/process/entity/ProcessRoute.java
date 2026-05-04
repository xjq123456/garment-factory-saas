package com.garment.production.domain.process.entity;

import com.garment.common.domain.AggregateRoot;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 工艺路线 - 聚合根
 *
 * 定义服装生产的工序流程，包含有序的工序步骤列表。
 * 每个款式可以关联一条或多条工艺路线。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ProcessRoute extends AggregateRoot {

    /** 路线编号 */
    private String routeCode;

    /** 路线名称 */
    private String routeName;

    /** 关联款式ID */
    private Long styleId;

    /** 描述 */
    private String description;

    /** 状态: ACTIVE-启用, INACTIVE-停用 */
    private String status;

    /** 工序步骤列表 */
    private List<ProcessStep> steps = new ArrayList<>();

    /**
     * 创建工艺路线
     */
    public static ProcessRoute create(String routeCode, String routeName, Long styleId) {
        ProcessRoute route = new ProcessRoute();
        route.setRouteCode(routeCode);
        route.setRouteName(routeName);
        route.setStyleId(styleId);
        route.setStatus("ACTIVE");
        return route;
    }

    /**
     * 添加工序步骤
     */
    public void addStep(ProcessStep step) {
        step.setRouteId(this.getId());
        // 自动分配序号
        int maxNo = steps.stream()
                .mapToInt(ProcessStep::getStepNo)
                .max()
                .orElse(0);
        step.setStepNo(maxNo + 1);
        this.steps.add(step);
    }

    /**
     * 移除工序步骤
     */
    public void removeStep(Long stepId) {
        this.steps.removeIf(s -> s.getId().equals(stepId));
        // 重新排序
        resequenceSteps();
    }

    /**
     * 重新排序工序步骤
     */
    private void resequenceSteps() {
        this.steps.sort(Comparator.comparingInt(ProcessStep::getStepNo));
        for (int i = 0; i < steps.size(); i++) {
            steps.get(i).setStepNo(i + 1);
        }
    }

    /**
     * 获取第一个工序（通常是裁剪）
     */
    public ProcessStep getFirstStep() {
        return steps.stream()
                .min(Comparator.comparingInt(ProcessStep::getStepNo))
                .orElse(null);
    }

    /**
     * 获取下一个工序
     */
    public ProcessStep getNextStep(Long currentStepId) {
        ProcessStep current = steps.stream()
                .filter(s -> s.getId().equals(currentStepId))
                .findFirst()
                .orElse(null);
        if (current == null) return null;

        return steps.stream()
                .filter(s -> s.getStepNo() == current.getStepNo() + 1)
                .findFirst()
                .orElse(null);
    }
}