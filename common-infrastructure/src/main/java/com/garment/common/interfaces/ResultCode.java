package com.garment.common.interfaces;

import lombok.Getter;

/**
 * 统一状态码枚举。
 * <p>
 * 规则：<br>
 * - 200: 成功<br>
 * - 4xx: 客户端错误（参数校验、认证、权限等）<br>
 * - 5xx: 服务端错误（业务异常、系统异常等）
 */
@Getter
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未认证或 Token 已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    BIZ_ERROR(500, "业务异常"),
    SYSTEM_ERROR(999, "系统内部错误");

    private final int code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}