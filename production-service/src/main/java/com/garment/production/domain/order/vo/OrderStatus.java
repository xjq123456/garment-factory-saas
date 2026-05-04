package com.garment.production.domain.order.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 生产工单状态
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {

    CREATED("CREATED", "已创建"),
    APPROVED("APPROVED", "已审批"),
    IN_PROGRESS("IN_PROGRESS", "生产中"),
    SUSPENDED("SUSPENDED", "暂停"),
    COMPLETED("COMPLETED", "已完成"),
    CLOSED("CLOSED", "已关闭");

    @EnumValue
    @JsonValue
    private final String code;
    private final String desc;

    /**
     * 校验状态流转是否合法
     */
    public void validateTransition(OrderStatus target) {
        boolean valid = switch (this) {
            case CREATED -> target == APPROVED || target == CLOSED;
            case APPROVED -> target == IN_PROGRESS || target == CLOSED;
            case IN_PROGRESS -> target == SUSPENDED || target == COMPLETED || target == CLOSED;
            case SUSPENDED -> target == IN_PROGRESS || target == CLOSED;
            case COMPLETED -> target == CLOSED;
            case CLOSED -> false;
        };
        if (!valid) {
            throw new IllegalStateException("工单状态不允许从 [" + this.desc + "] 变更为 [" + target.desc + "]");
        }
    }
}