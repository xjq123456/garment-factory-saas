package com.garment.common.domain;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类。
 * <p>
 * 基于 jjwt 0.12.x，提供 Token 签发、解析、刷新能力。
 * <p>
 * 使用方式：通过 {@link com.garment.common.config.CommonAutoConfiguration} 自动注入，
 * 或手动创建 {@code new JwtUtils(secret, expiration)}。
 */
@Slf4j
public class JwtUtils {

    private final SecretKey key;
    private final long expirationMs;

    /**
     * @param secret     签名密钥（至少 32 字节 / 256 位）
     * @param expiration Token 有效期（毫秒）
     */
    public JwtUtils(String secret, long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    /**
     * 签发 Token。
     *
     * @param subject  用户标识（通常为用户 ID）
     * @param claims   自定义声明（租户 ID、角色等）
     * @return 签名后的 JWT 字符串
     */
    public String generateToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        JwtBuilder builder = Jwts.builder()
                .id(java.util.UUID.randomUUID().toString().replace("-", ""))
                .subject(subject)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key, Jwts.SIG.HS256);

        if (claims != null && !claims.isEmpty()) {
            builder.claims(claims);
        }

        return builder.compact();
    }

    /**
     * 签发 Token（无额外声明）。
     */
    public String generateToken(String subject) {
        return generateToken(subject, null);
    }

    /**
     * 解析并验证 Token，返回 Claims。
     *
     * @throws JwtException Token 无效或已过期时抛出
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 安全解析 Token，失败时返回 null 而非抛出异常。
     */
    public Claims parseTokenSafe(String token) {
        try {
            return parseToken(token);
        } catch (JwtException e) {
            log.debug("JWT 解析失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取用户标识（subject）。
     */
    public String getSubject(String token) {
        Claims claims = parseTokenSafe(token);
        return claims != null ? claims.getSubject() : null;
    }

    /**
     * 判断 Token 是否已过期。
     */
    public boolean isTokenExpired(String token) {
        Claims claims = parseTokenSafe(token);
        return claims == null || claims.getExpiration().before(new Date());
    }
}