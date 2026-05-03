package com.garmentfactory.gateway.filter.access;

import com.garmentfactory.gateway.util.RemoteClientAddressResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ConditionalOnProperty(name = "gateway.audit.enabled", havingValue = "true", matchIfMissing = true)
public class HttpExchangeAuditFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return -100;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startNanos = System.nanoTime();
        ServerHttpRequest request = exchange.getRequest();
        return chain.filter(exchange).doFinally(signalType -> {
            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
            HttpStatusCode status = exchange.getResponse().getStatusCode();
            int code = status != null ? status.value() : 0;
            if (log.isInfoEnabled()) {
                log.info("{} {} -> {} {}ms client={}",
                        request.getMethod(),
                        request.getURI().getRawPath(),
                        code,
                        elapsedMs,
                        RemoteClientAddressResolver.resolve(request));
            }
        });
    }
}
