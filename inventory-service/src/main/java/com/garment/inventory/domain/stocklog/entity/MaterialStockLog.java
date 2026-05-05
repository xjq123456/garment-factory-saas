package com.garment.inventory.domain.stocklog.entity;

import com.garment.common.domain.ValueObject;
import com.garment.inventory.domain.stocklog.vo.ChangeType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 原材料库存变动日志实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaterialStockLog extends ValueObject {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * 原材料ID
     */
    private Long materialId;

    /**
     * 变动类型
     */
    private ChangeType changeType;

    /**
     * 变动数量
     */
    private BigDecimal changeQty;

    /**
     * 变动前数量
     */
    private BigDecimal beforeQty;

    /**
     * 变动后数量
     */
    private BigDecimal afterQty;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 业务单号
     */
    private String bizNo;

    /**
     * 批次号
     */
    private String batchNo;

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
     * 创建原材料库存变动日志
     */
    public static MaterialStockLog create(Long warehouseId, Long materialId, ChangeType changeType,
                                           BigDecimal changeQty, BigDecimal beforeQty, BigDecimal afterQty,
                                           String bizType, String bizNo, String batchNo,
                                           Long operatorId, String operatorName, String remark) {
        MaterialStockLog log = new MaterialStockLog();
        log.setWarehouseId(warehouseId);
        log.setMaterialId(materialId);
        log.setChangeType(changeType);
        log.setChangeQty(changeQty);
        log.setBeforeQty(beforeQty);
        log.setAfterQty(afterQty);
        log.setBizType(bizType);
        log.setBizNo(bizNo);
        log.setBatchNo(batchNo);
        log.setOperatorId(operatorId);
        log.setOperatorName(operatorName);
        log.setRemark(remark);
        log.setCreateTime(LocalDateTime.now());
        return log;
    }
}