package com.garment.common.domain;

import lombok.Getter;

/**
 * 聚合根基类。
 * <p>
 * DDD 中的聚合根是事务一致性边界，负责维护领域不变量。
 * <p>
 * 领域事件采用函数式风格：业务方法直接返回 {@link DomainEvent}，
 * 由应用层负责发布。聚合根不持有事件列表，保持领域层纯净。
 * <p>
 * 继承 {@link BaseDomainEntity} 获得领域标识（id、tenantId）和审计信息，
 * 不依赖任何持久化框架注解。
 *
 * @see com.garment.common.infrastructure.BaseEntity 持久化基类（仅 DO 使用）
 */
@Getter
public abstract class AggregateRoot extends BaseDomainEntity {
}
