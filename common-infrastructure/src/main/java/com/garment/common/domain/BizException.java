package com.garment.common.domain;

import com.garment.common.interfaces.ResultCode;
import lombok.Getter;

/**
 * 业务异常基类。
 * <p>
 * 所有业务层抛出的已知异常均应使用此类或其子类，
 * 由全局异常处理器统一捕获并转换为标准响应。
 */
@Getter
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int code;

    public BizException(String message) {
        super(message);
        this.code = ResultCode.BIZ_ERROR.getCode();
    }

    public BizException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }

    public BizException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
    }

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }
}