package com.garment.common.infrastructure;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.garment.common.domain.TenantContext;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;

/**
 * MyBatis Plus 多租户插件配置。
 * <p>
 * 自动为 SQL 语句追加 {@code tenant_id = ?} 条件，
 * 确保租户数据逻辑隔离。
 * <p>
 * 业务服务在 {@code @Configuration} 中引入即可：
 * <pre>
 *   @Bean
 *   public MybatisPlusInterceptor mybatisPlusInterceptor() {
 *       return MybatisPlusTenantConfig.createInterceptor();
 *   }
 * </pre>
 */
public final class MybatisPlusTenantConfig {

    /** 需要排除的表（全局共享，不含 tenant_id 列） */
    private static final java.util.List<String> IGNORE_TABLES = java.util.List.of(
            "sys_tenant",
            "sys_user"
    );

    private MybatisPlusTenantConfig() {}

    /**
     * 创建带租户拦截器的 MybatisPlusInterceptor 实例。
     */
    public static MybatisPlusInterceptor createInterceptor() {
        TenantLineHandler handler = new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                Long tenantId = TenantContext.getTenantId();
                return new LongValue(tenantId != null ? tenantId : 0L);
            }

            @Override
            public boolean ignoreTable(String tableName) {
                return IGNORE_TABLES.contains(tableName.toLowerCase());
            }
        };
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(handler));
        return interceptor;
    }
}