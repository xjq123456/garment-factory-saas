package com.garment.common.interfaces;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应体。
 *
 * @param <T> 数据类型
 */
@Data
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 状态码 */
    private int code;

    /** 提示消息 */
    private String msg;

    /** 响应数据 */
    private T data;

    private R() {}

    // ===================== 成功 =====================

    public static <T> R<T> ok() {
        return ok(null);
    }

    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setCode(ResultCode.SUCCESS.getCode());
        r.setMsg(ResultCode.SUCCESS.getMsg());
        r.setData(data);
        return r;
    }

    public static <T> R<T> ok(T data, String msg) {
        R<T> r = new R<>();
        r.setCode(ResultCode.SUCCESS.getCode());
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    // ===================== 失败 =====================

    public static <T> R<T> fail(String msg) {
        return fail(ResultCode.BIZ_ERROR, msg);
    }

    public static <T> R<T> fail(ResultCode resultCode) {
        return fail(resultCode, resultCode.getMsg());
    }

    public static <T> R<T> fail(ResultCode resultCode, String msg) {
        R<T> r = new R<>();
        r.setCode(resultCode.getCode());
        r.setMsg(msg);
        return r;
    }

    public static <T> R<T> fail(int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    // ===================== 便捷判断 =====================

    public boolean isSuccess() {
        return ResultCode.SUCCESS.getCode() == this.code;
    }
}