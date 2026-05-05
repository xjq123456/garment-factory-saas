package com.garmentfactory.gateway.filter.auth;

import com.garmentfactory.gateway.config.GatewayJwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * JWT 认证全局过滤器。
 * <p>
 * 职责：
 * 1. 白名单路径直接放行
 * 2. 解析 Authorization Bearer Token
 * 3. 验证 Token 有效性
 * 4. 将用户信息（userId、tenantId）注入请求头传递给下游微服务
 *
 * @author garment-factory-saas
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "gateway.jwt.enabled", havingValue = "true", matchIfMissing = true)
public class JwtAuthGlobalFilter implements GlobalFilter, Ordered {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USERNAME = "X-Username";
    private static final String HEADER_TENANT_ID = "X-Tenant-Id";
    private static final String HEADER_ROLES = "X-Roles";
    private static final String CLAIM_TENANT_ID = "tenantId";
    private static final String CLAIM_USERNAME = "username";
    private static final String CLAIM_ROLES = "roles";

    private final GatewayJwtProperties jwtProperties;
    private final SecretKey secretKey;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtAuthGlobalFilter(GatewayJwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(
                jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 1. 白名单放行
        if (isWhitelisted(path)) {
            log.debug("白名单放行: {}", path);
            return chain.filter(exchange);
        }

        // 2. 提取 Token
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            log.warn("缺少 Authorization 头: {}", path);
            return unauthorized(exchange, "未提供认证令牌");
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        // 3. 解析验证 Token
        Claims claims;
        try {
            claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            log.warn("JWT 验证失败: {} — {}", path, e.getMessage());
            return unauthorized(exchange, "认证令牌无效或已过期");
        }

        // 4. 提取用户信息并注入请求头
        String userId = claims.getSubject();
        Object tenantIdObj = claims.get(CLAIM_TENANT_ID);
        String tenantId = tenantIdObj != null ? String.valueOf(tenantIdObj) : null;
        Object usernameObj = claims.get(CLAIM_USERNAME);
        String username = usernameObj != null ? String.valueOf(usernameObj) : null;
        Object rolesObj = claims.get(CLAIM_ROLES);
        String roles = null;
        if (rolesObj instanceof java.util.List<?> roleList) {
            roles = roleList.stream()
                    .map(String::valueOf)
                    .collect(java.util.stream.Collectors.joining(","));
        }

        ServerHttpRequest.Builder requestBuilder = request.mutate()
                .header(HEADER_USER_ID, userId);
        if (tenantId != null && !tenantId.isEmpty()) {
            requestBuilder.header(HEADER_TENANT_ID, tenantId);
        }
        if (username != null && !username.isEmpty()) {
            requestBuilder.header(HEADER_USERNAME, username);
        }
        if (roles != null && !roles.isEmpty()) {
            requestBuilder.header(HEADER_ROLES, roles);
        }
        ServerHttpRequest mutatedRequest = requestBuilder.build();

        log.debug("JWT 认证通过: userId={}, tenantId={}, username={}, path={}", userId, tenantId, username, path);

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    /**
     * 检查路径是否匹配白名单。
     */
    private boolean isWhitelisted(String path) {
        List<String> whitelist = jwtProperties.getWhitelist();
        if (whitelist == null || whitelist.isEmpty()) {
            return false;
        }
        return whitelist.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    /**
     * 返回 401 Unauthorized JSON 响应。
     */
    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = String.format(
                "{\"code\":401,\"message\":\"%s\",\"data\":null}", message);
        org.springframework.core.io.buffer.DataBuffer buffer =
                response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        // 在缓存 body 过滤器(-200) 和审计过滤器(-100) 之后执行，优先级 -50
        return -50;
    }
}