package com.garment.common.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 领域实体基类。
 * <p>
 * 纯领域层抽象，不包含任何持久化框架注解（如 MyBatis-Plus、JPA 等）。
 * 仅包含领域层面有意义的标识和审计信息。
 * <p>
 * 持久化相关的能力（雪花主键、逻辑删除、乐观锁等）由 DO 层的 {@code com.garment.common.infrastructure.BaseEntity} 负责。
 *
 * @see com.garment.common.infrastructure.BaseEntity 持久化基类（仅 DO 使用）
 */
@Getter
@Setter
public abstract class BaseDomainEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 ID */
    protected Long id;

    /** 租户 ID */
    protected Long tenantId;

    /** 创建时间 */
    protected LocalDateTime createTime;

    /** 创建人 */
    protected Long createBy;

    /** 最后更新时间 */
    protected LocalDateTime updateTime;

    /** 最后更新人 */
    protected Long updateBy;

    /** 乐观锁版本号 */
    protected Integer version;

    // ==================== 兼容别名方法 ====================
    // 部分服务使用 createdAt/createdBy 命名，提供兼容访问器

    public LocalDateTime getCreatedAt() { return createTime; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createTime = createdAt; }

    public Long getCreatedBy() { return createBy; }
    public void setCreatedBy(Long createdBy) { this.createBy = createdBy; }

    public LocalDateTime getUpdatedAt() { return updateTime; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updateTime = updatedAt; }

    public Long getUpdatedBy() { return updateBy; }
    public void setUpdatedBy(Long updatedBy) { this.updateBy = updatedBy; }
}
