package com.garment.inventory.domain.stocklog.entity;

import com.garment.common.domain.ValueObject;
import com.garment.inventory.domain.stocklog.vo.ChangeType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 成品库存变动日志实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StockLog extends ValueObject {

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * 变动类型
     */
    private ChangeType changeType;

    /**
     * 变动数量
     */
    private Integer changeQty;

    /**
     * 变动前数量
     */
    private Integer beforeQty;

    /**
     * 变动后数量
     */
    private Integer afterQty;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 业务单号
     */
    private String bizNo;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建库存变动日志
     */
    public static StockLog create(Long warehouseId, Long skuId, ChangeType changeType,
                                   int changeQty, int beforeQty, int afterQty,
                                   String bizType, String bizNo,
                                   Long operatorId, String operatorName, String remark) {
        StockLog log = new StockLog();
        log.setWarehouseId(warehouseId);
        log.setSkuId(skuId);
        log.setChangeType(changeType);
        log.setChangeQty(changeQty);
        log.setBeforeQty(beforeQty);
        log.setAfterQty(afterQty);
        log.setBizType(bizType);
        log.setBizNo(bizNo);
        log.setOperatorId(operatorId);
        log.setOperatorName(operatorName);
        log.setRemark(remark);
        log.setCreateTime(LocalDateTime.now());
        return log;
    }
}