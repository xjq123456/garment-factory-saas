package com.garment.production.domain.task.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 生产任务状态
 */
@Getter
@AllArgsConstructor
public enum TaskStatus {

    PENDING("PENDING", "待分配"),
    ASSIGNED("ASSIGNED", "已分配"),
    IN_PROGRESS("IN_PROGRESS", "进行中"),
    PAUSED("PAUSED", "暂停"),
    COMPLETED("COMPLETED", "已完成"),
    CANCELLED("CANCELLED", "已取消");

    @EnumValue
    @JsonValue
    private final String code;
    private final String desc;

    /**
     * 校验状态流转是否合法
     */
    public void validateTransition(TaskStatus target) {
        boolean valid = switch (this) {
            case PENDING -> target == ASSIGNED || target == CANCELLED;
            case ASSIGNED -> target == IN_PROGRESS || target == CANCELLED;
            case IN_PROGRESS -> target == PAUSED || target == COMPLETED || target == CANCELLED;
            case PAUSED -> target == IN_PROGRESS || target == CANCELLED;
            case COMPLETED -> false;
            case CANCELLED -> false;
        };
        if (!valid) {
            throw new IllegalStateException("任务状态不允许从 [" + this.desc + "] 变更为 [" + target.desc + "]");
        }
    }
}