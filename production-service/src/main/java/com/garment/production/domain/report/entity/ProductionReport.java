package com.garment.production.domain.report.entity;

import com.garment.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 报工记录 - 实体
 *
 * 工人完成一定数量的产品后进行报工，
 * 记录完成数量、合格数量、不良品数量和工时。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ProductionReport extends BaseEntity {

    /** 主键ID */
    private Long id;

    /** 报工编号 */
    private String reportNo;

    /** 生产工单ID */
    private Long orderId;

    /** 生产任务ID */
    private Long taskId;

    /** 工位ID */
    private Long stationId;

    /** 工人ID */
    private Long workerId;

    /** 工人姓名 */
    private String workerName;

    /** 报工数量 */
    private Integer reportQty;

    /** 合格数量 */
    private Integer qualifiedQty;

    /** 不良品数量 */
    private Integer defectiveQty;

    /** 工时(小时) */
    private BigDecimal workHours;

    /** 报工时间 */
    private LocalDateTime reportTime;

    /** 备注 */
    private String remark;

    /**
     * 创建报工记录
     */
    public static ProductionReport create(String reportNo, Long orderId, Long taskId,
                                          Long workerId, String workerName) {
        ProductionReport report = new ProductionReport();
        report.setReportNo(reportNo);
        report.setOrderId(orderId);
        report.setTaskId(taskId);
        report.setWorkerId(workerId);
        report.setWorkerName(workerName);
        report.setReportTime(LocalDateTime.now());
        return report;
    }

    /**
     * 提交报工
     */
    public void submit(int reportQty, int qualifiedQty, int defectiveQty) {
        if (reportQty != qualifiedQty + defectiveQty) {
            throw new IllegalArgumentException("报工数量必须等于合格数量与不良品数量之和");
        }
        this.reportQty = reportQty;
        this.qualifiedQty = qualifiedQty;
        this.defectiveQty = defectiveQty;
    }

    /**
     * 获取合格率
     */
    public double getQualifiedRate() {
        if (reportQty == null || reportQty == 0) return 0;
        return (qualifiedQty * 100.0) / reportQty;
    }
}