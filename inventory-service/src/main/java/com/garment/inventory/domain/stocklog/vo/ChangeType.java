package com.garment.inventory.domain.stocklog.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 库存变动类型
 */
@Getter
public enum ChangeType {

    INBOUND(1, "入库"),
    OUTBOUND(2, "出库"),
    TRANSFER_IN(3, "调拨入"),
    TRANSFER_OUT(4, "调拨出"),
    STOCKTAKE_PROFIT(5, "盘点盈"),
    STOCKTAKE_LOSS(6, "盘点亏"),
    LOCK(7, "锁定"),
    UNLOCK(8, "解锁");

    @EnumValue
    @JsonValue
    private final int code;
    private final String desc;

    ChangeType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ChangeType of(int code) {
        for (ChangeType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid change type code: " + code);
    }
}