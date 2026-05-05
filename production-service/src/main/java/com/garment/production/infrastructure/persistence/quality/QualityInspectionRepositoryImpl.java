package com.garment.production.infrastructure.persistence.quality;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garment.common.interfaces.PageResult;
import com.garment.production.domain.quality.entity.QualityInspection;
import com.garment.production.domain.quality.repository.QualityInspectionRepository;
import com.garment.production.domain.quality.vo.InspectionResult;
import com.garment.production.domain.quality.vo.InspectionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 质检记录仓储实现
 */
@Repository
@RequiredArgsConstructor
public class QualityInspectionRepositoryImpl implements QualityInspectionRepository {

    private final QualityInspectionMapper mapper;
    private final QualityInspectionConverter converter;

    @Override
    public void save(QualityInspection inspection) {
        QualityInspectionDO DO = converter.toDO(inspection);
        mapper.insert(DO);
        inspection.setId(DO.getId());
    }

    @Override
    public void update(QualityInspection inspection) {
        mapper.updateById(converter.toDO(inspection));
    }

    @Override
    public Optional<QualityInspection> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id))
                .map(converter::toEntity);
    }

    @Override
    public Optional<QualityInspection> findByInspectionNo(String inspectionNo) {
        LambdaQueryWrapper<QualityInspectionDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QualityInspectionDO::getInspectionNo, inspectionNo);
        return Optional.ofNullable(mapper.selectOne(wrapper))
                .map(converter::toEntity);
    }

    @Override
    public List<QualityInspection> findByOrderId(Long orderId) {
        LambdaQueryWrapper<QualityInspectionDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QualityInspectionDO::getOrderId, orderId)
                .orderByDesc(QualityInspectionDO::getCreateTime);
        return mapper.selectList(wrapper).stream()
                .map(converter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<QualityInspection> findByTaskId(Long taskId) {
        LambdaQueryWrapper<QualityInspectionDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QualityInspectionDO::getTaskId, taskId);
        return mapper.selectList(wrapper).stream()
                .map(converter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<QualityInspection> findPage(Long tenantId, Long orderId, InspectionType type,
                                                    InspectionResult result, int page, int size) {
        LambdaQueryWrapper<QualityInspectionDO> wrapper = new LambdaQueryWrapper<>();

        if (tenantId != null) {
            wrapper.eq(QualityInspectionDO::getTenantId, tenantId);
        }
        if (orderId != null) {
            wrapper.eq(QualityInspectionDO::getOrderId, orderId);
        }
        if (type != null) {
            wrapper.eq(QualityInspectionDO::getInspectionType, type.getCode());
        }
        if (result != null) {
            wrapper.eq(QualityInspectionDO::getResult, result.getCode());
        }
        wrapper.orderByDesc(QualityInspectionDO::getCreateTime);

        Page<QualityInspectionDO> pageResult = mapper.selectPage(new Page<>(page, size), wrapper);

        List<QualityInspection> records = pageResult.getRecords().stream()
                .map(converter::toEntity)
                .collect(Collectors.toList());

        return new PageResult<>(records, pageResult.getTotal(), page, size);
    }

    @Override
    public long countReject(Long tenantId, Long orderId) {
        LambdaQueryWrapper<QualityInspectionDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QualityInspectionDO::getTenantId, tenantId)
                .eq(QualityInspectionDO::getOrderId, orderId)
                .eq(QualityInspectionDO::getResult, InspectionResult.REJECT.getCode());
        return mapper.selectCount(wrapper);
    }
}