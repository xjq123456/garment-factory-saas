package com.garment.user.domain.identity.event;

import com.garment.common.domain.DomainEvent;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 用户注册领域事件
 * <p>
 * 当新用户成功注册时发布此事件。其他服务可以监听此事件执行后续操作，
 * 例如：rbac-service 初始化默认角色、audit-service 记录审计日志等。
 * </p>
 *
 * @author garment-factory-saas
 */
@Getter
public class UserRegisteredEvent extends DomainEvent {

    /** 用户ID */
    private final Long userId;

    /** 用户名 */
    private final String username;

    /** 租户ID */
    private final Long tenantId;

    /** 注册方式（PHONE / EMAIL） */
    private final String registerType;

    /**
     * 构造用户注册事件
     *
     * @param userId       用户ID
     * @param username     用户名
     * @param tenantId     租户ID
     * @param registerType 注册方式
     */
    public UserRegisteredEvent(Long userId, String username, Long tenantId, String registerType) {
        super();
        this.userId = userId;
        this.username = username;
        this.tenantId = tenantId;
        this.registerType = registerType;
    }
}