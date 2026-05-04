package com.garment.common.infrastructure;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通用实体基类。
 * <p>
 * 所有业务实体继承此类，自动获得以下能力：
 * <ul>
 *   <li>主键（雪花算法）</li>
 *   <li>逻辑删除标记</li>
 *   <li>租户 ID（多租户隔离）</li>
 *   <li>审计字段（创建/更新时间、创建/更新人）</li>
 *   <li>乐观锁版本号</li>
 * </ul>
 */
@Data
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键（雪花算法自动生成） */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 租户 ID（由多租户插件自动填充，INSERT 时不需要手动设置） */
    @TableField("tenant_id")
    private Long tenantId;

    /** 逻辑删除标记：0=正常，1=已删除 */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    /** 乐观锁版本号 */
    @Version
    @TableField("version")
    private Integer version;

    /** 创建时间 */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /** 创建人 */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    /** 最后更新时间 */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /** 最后更新人 */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;
}