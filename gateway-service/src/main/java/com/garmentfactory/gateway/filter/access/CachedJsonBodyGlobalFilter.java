package com.garmentfactory.gateway.filter.access;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Caches JSON POST bodies so downstream filters and the proxied service can read the stream again.
 */
@Component
@ConditionalOnProperty(name = "gateway.cache-request-body.enabled", havingValue = "true")
public class CachedJsonBodyGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return -200;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (!HttpMethod.POST.equals(request.getMethod())) {
            return chain.filter(exchange);
        }
        String contentType = request.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
        if (!StringUtils.hasText(contentType)
                || !contentType.toLowerCase().contains(MediaType.APPLICATION_JSON_VALUE)) {
            return chain.filter(exchange);
        }
        return DataBufferUtils.join(request.getBody())
                .flatMap(dataBuffer -> {
                    int n = dataBuffer.readableByteCount();
                    if (n == 0) {
                        DataBufferUtils.release(dataBuffer);
                        return chain.filter(exchange);
                    }
                    byte[] bytes = new byte[n];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    Flux<DataBuffer> cached = Flux.defer(() -> {
                        DataBuffer buf = exchange.getResponse().bufferFactory().wrap(bytes);
                        return Flux.just(buf);
                    });
                    ServerHttpRequest decorated = new ServerHttpRequestDecorator(request) {
                        @Override
                        public Flux<DataBuffer> getBody() {
                            return cached;
                        }
                    };
                    return chain.filter(exchange.mutate().request(decorated).build());
                })
                .switchIfEmpty(Mono.defer(() -> chain.filter(exchange)));
    }
}
