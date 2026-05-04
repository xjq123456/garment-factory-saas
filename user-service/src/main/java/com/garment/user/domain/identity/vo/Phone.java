package com.garment.user.domain.identity.vo;

import com.garment.common.domain.ValueObject;
import lombok.Getter;

import java.util.regex.Pattern;

/**
 * 手机号值对象
 * <p>
 * 封装手机号的校验逻辑与不可变特性，确保领域模型中的手机号始终合法。
 * 作为值对象，Phone 没有唯一标识，两个相同号码的 Phone 被视为相等。
 * </p>
 *
 * @author garment-factory-saas
 */
@Getter
public class Phone extends ValueObject {

    /** 手机号正则：11位数字，1开头 */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    /** 手机号值 */
    private final String value;

    /**
     * 构造手机号值对象
     *
     * @param value 手机号字符串
     * @throws IllegalArgumentException 当手机号格式不合法时抛出
     */
    public Phone(String value) {
        if (value == null || !PHONE_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("手机号格式不合法: " + value);
        }
        this.value = value;
    }

    /**
     * 隐藏中间四位号码，用于脱敏展示
     *
     * @return 脱敏后的手机号，如 138****1234
     */
    public String toMasked() {
        return value.substring(0, 3) + "****" + value.substring(7);
    }

    @Override
    public String toString() {
        return value;
    }
}