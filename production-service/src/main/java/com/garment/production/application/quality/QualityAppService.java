package com.garment.production.application.quality;

import com.garment.common.domain.AuthUserContext;
import com.garment.common.domain.BizException;
import com.garment.common.interfaces.PageResult;
import com.garment.production.application.quality.dto.CreateInspectionCommand;
import com.garment.production.application.quality.dto.QualityInspectionVO;
import com.garment.production.application.quality.dto.SubmitInspectionCommand;
import com.garment.production.domain.quality.entity.QualityInspection;
import com.garment.production.domain.quality.repository.QualityInspectionRepository;
import com.garment.production.domain.quality.vo.InspectionResult;
import com.garment.production.domain.quality.vo.InspectionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 质检管理应用服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QualityAppService {

    private final QualityInspectionRepository inspectionRepository;

    /**
     * 创建质检记录
     */
    @Transactional
    public QualityInspectionVO createInspection(CreateInspectionCommand cmd) {
        String inspectionNo = generateInspectionNo();
        QualityInspection inspection = QualityInspection.create(
                inspectionNo, cmd.getOrderId(),
                InspectionType.valueOf(cmd.getInspectionType()),
                cmd.getInspectQty()
        );
        inspection.setTaskId(cmd.getTaskId());
        inspection.setRemark(cmd.getRemark());

        inspectionRepository.save(inspection);

        log.info("质检记录创建成功: inspectionNo={}, type={}, inspectQty={}",
                inspectionNo, cmd.getInspectionType(), cmd.getInspectQty());

        return toVO(inspection);
    }

    /**
     * 提交质检结果
     */
    @Transactional
    public QualityInspectionVO submitResult(SubmitInspectionCommand cmd) {
        QualityInspection inspection = inspectionRepository.findById(cmd.getInspectionId())
                .orElseThrow(() -> new BizException("质检记录不存在: " + cmd.getInspectionId()));

        InspectionResult result = InspectionResult.valueOf(cmd.getResult());

        inspection.submitResult(cmd.getPassQty(), cmd.getRejectQty(), result,
                cmd.getInspectorId(), cmd.getInspectorName());

        if (cmd.getDefectTypes() != null || cmd.getDefectDesc() != null) {
            inspection.recordDefects(cmd.getDefectTypes(), cmd.getDefectDesc());
        }

        if (cmd.getRemark() != null) {
            inspection.setRemark(cmd.getRemark());
        }

        inspectionRepository.update(inspection);

        log.info("质检结果提交: inspectionNo={}, result={}, passRate={:.2f}%",
                inspection.getInspectionNo(), result.getDesc(), inspection.getPassRate());

        return toVO(inspection);
    }

    /**
     * 查询质检记录详情
     */
    public QualityInspectionVO getInspection(Long inspectionId) {
        QualityInspection inspection = inspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new BizException("质检记录不存在: " + inspectionId));
        return toVO(inspection);
    }

    /**
     * 按工单查询质检记录
     */
    public List<QualityInspectionVO> listByOrder(Long orderId) {
        return inspectionRepository.findByOrderId(orderId).stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    /**
     * 分页查询质检记录
     */
    public PageResult<QualityInspectionVO> findInspections(Long orderId, InspectionType type,
                                                            InspectionResult result,
                                                            int page, int size) {
        Long tenantId = AuthUserContext.requireTenantId();
        PageResult<QualityInspection> pageResult = inspectionRepository.findPage(
                tenantId, orderId, type, result, page, size);

        List<QualityInspectionVO> voList = pageResult.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        return new PageResult<>(voList, pageResult.getTotal(), page, size);
    }

    // ============ private methods ============

    private String generateInspectionNo() {
        return "QI" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    private QualityInspectionVO toVO(QualityInspection inspection) {
        QualityInspectionVO vo = new QualityInspectionVO();
        vo.setId(inspection.getId());
        vo.setInspectionNo(inspection.getInspectionNo());
        vo.setOrderId(inspection.getOrderId());
        vo.setTaskId(inspection.getTaskId());
        vo.setInspectionType(inspection.getInspectionType());
        vo.setInspectQty(inspection.getInspectQty());
        vo.setPassQty(inspection.getPassQty());
        vo.setRejectQty(inspection.getRejectQty());
        vo.setDefectTypes(inspection.getDefectTypes());
        vo.setDefectDesc(inspection.getDefectDesc());
        vo.setResult(inspection.getResult());
        vo.setInspectorId(inspection.getInspectorId());
        vo.setInspectorName(inspection.getInspectorName());
        vo.setInspectionTime(inspection.getInspectionTime());
        vo.setPassRate(inspection.getPassRate());
        vo.setQualified(inspection.isQualified());
        vo.setRemark(inspection.getRemark());
        vo.setCreateTime(inspection.getCreateTime());
        return vo;
    }
}