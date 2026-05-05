package com.garment.common.infrastructure;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.garment.common.domain.AuthUserContext;
import com.garment.common.domain.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * MyBatis Plus 审计字段自动填充。
 * <p>
 * INSERT 时自动填充 created_at、created_by、tenant_id；<br>
 * UPDATE 时自动填充 updated_at、updated_by。
 */
@Slf4j
public class AuditMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, now);
        this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, now);
        // createdBy / updatedBy 从 AuthUserContext 获取当前用户 ID
        Long userId = AuthUserContext.getUserId();
        long uid = userId != null ? userId : 0L;
        this.strictInsertFill(metaObject, "createdBy", Long.class, uid);
        this.strictInsertFill(metaObject, "updatedBy", Long.class, uid);
        // tenant_id 由 TenantLineInnerInterceptor 自动填充到 SQL，此处保证实体字段也有值
        if (metaObject.hasGetter("tenantId")) {
            Long tenantId = TenantContext.getTenantId();
            if (tenantId != null) {
                this.strictInsertFill(metaObject, "tenantId", Long.class, tenantId);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
        Long userId = AuthUserContext.getUserId();
        this.strictUpdateFill(metaObject, "updatedBy", Long.class, userId != null ? userId : 0L);
    }
}