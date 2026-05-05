package com.garment.search.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Elasticsearch 配置。
 * <p>
 * Spring Boot 3.1.5 通过 {@code spring.elasticsearch.*} 属性自动配置
 * {@link org.springframework.data.elasticsearch.core.ElasticsearchOperations}，
 * 无需手动创建客户端 Bean。
 * <p>
 * 连接参数在 application.yml 中配置：
 * <pre>
 * spring:
 *   elasticsearch:
 *     uris: http://localhost:9200
 *     username: elastic
 *     password: xxx
 * </pre>
 */
@Configuration
public class ElasticsearchConfig {

    @Value("${search.index.prefix:garment_}")
    private String indexPrefix;

    /**
     * 索引名称前缀，供其他组件注入使用。
     */
    public String getIndexPrefix() {
        return indexPrefix;
    }
}
