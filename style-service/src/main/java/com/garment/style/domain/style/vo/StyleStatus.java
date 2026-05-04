package com.garment.style.domain.style.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum StyleStatus {
    DRAFT(0, "草稿"), PUBLISHED(1, "已发布"), AUDITED(2, "已审核"), DISABLED(3, "已停用");

    @EnumValue @JsonValue
    private final int code;
    private final String desc;

    StyleStatus(int code, String desc) { this.code = code; this.desc = desc; }
}
