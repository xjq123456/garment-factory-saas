package com.garmentfactory.gateway.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;

/**
 * Resolves a client IP for logging or auditing behind reverse proxies.
 */
public final class RemoteClientAddressResolver {

    private RemoteClientAddressResolver() {
    }

    public static String resolve(ServerHttpRequest request) {
        String forwarded = firstNonBlank(
                request.getHeaders().getFirst("X-Forwarded-For"),
                request.getHeaders().getFirst("X-Real-IP"));
        if (StringUtils.hasText(forwarded)) {
            int comma = forwarded.indexOf(',');
            return comma > 0 ? forwarded.substring(0, comma).trim() : forwarded.trim();
        }
        InetSocketAddress remote = request.getRemoteAddress();
        if (remote != null && remote.getAddress() != null) {
            return remote.getAddress().getHostAddress();
        }
        return "unknown";
    }

    private static String firstNonBlank(String a, String b) {
        if (StringUtils.hasText(a)) {
            return a;
        }
        if (StringUtils.hasText(b)) {
            return b;
        }
        return null;
    }
}
