package com.garment.common.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * 值对象基类。
 * <p>
 * DDD 中的值对象通过属性值判断相等性，不可变。
 * 子类应将所有字段声明为 {@code final}，并提供全参构造器。
 */
public abstract class ValueObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(this.toString(), o.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.toString());
    }
}