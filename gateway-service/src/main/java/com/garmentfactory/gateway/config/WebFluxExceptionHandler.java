package com.garmentfactory.gateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 网关全局异常处理器（WebFlux）。
 * <p>
 * 统一捕获路由转发失败、下游服务不可用等异常，返回标准 JSON 格式。
 *
 * @author garment-factory-saas
 */
@Slf4j
@Order(-1) // 优先于默认的 ErrorWebExceptionHandler
@Component
@RequiredArgsConstructor
public class WebFluxExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        // 响应已提交，无法再写入
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        HttpStatus status;
        String message;

        if (ex instanceof ResponseStatusException) {
            ResponseStatusException rse = (ResponseStatusException) ex;
            status = HttpStatus.valueOf(rse.getStatusCode().value());
            message = rse.getReason() != null ? rse.getReason() : status.getReasonPhrase();
        } else {
            // 网关转发异常（下游服务不可用 / 连接超时等）
            status = HttpStatus.BAD_GATEWAY;
            message = "下游服务暂时不可用，请稍后重试";
            log.error("网关异常: {} — {}", exchange.getRequest().getPath(), ex.getMessage(), ex);
        }

        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", status.value());
        body.put("message", message);
        body.put("data", null);

        String json;
        try {
            json = objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            json = "{\"code\":" + status.value() + ",\"message\":\"" + message + "\",\"data\":null}";
        }

        DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}