package com.garment.user.domain.identity.vo;

import com.garment.common.domain.ValueObject;
import lombok.Getter;

import java.util.regex.Pattern;

/**
 * 邮箱地址值对象
 * <p>
 * 封装邮箱地址的格式校验逻辑，确保领域模型中的邮箱始终合法。
 * 作为值对象，Email 没有唯一标识，两个相同地址的 Email 被视为相等。
 * </p>
 *
 * @author garment-factory-saas
 */
@Getter
public class Email extends ValueObject {

    /** 邮箱正则表达式 */
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    /** 邮箱地址值 */
    private final String value;

    /**
     * 构造邮箱值对象
     *
     * @param value 邮箱地址字符串
     * @throws IllegalArgumentException 当邮箱格式不合法时抛出
     */
    public Email(String value) {
        if (value == null || !EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("邮箱格式不合法: " + value);
        }
        this.value = value.toLowerCase().trim();
    }

    /**
     * 获取邮箱用户名部分（@ 之前）
     *
     * @return 邮箱用户名
     */
    public String getLocalPart() {
        return value.substring(0, value.indexOf('@'));
    }

    @Override
    public String toString() {
        return value;
    }
}