package com.garment.production.infrastructure.persistence.report;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.garment.production.domain.report.entity.ProductionReport;
import com.garment.production.domain.report.repository.ProductionReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 报工记录仓储实现
 */
@Repository
@RequiredArgsConstructor
public class ProductionReportRepositoryImpl implements ProductionReportRepository {

    private final ProductionReportMapper mapper;
    private final ProductionReportConverter converter;

    @Override
    public void save(ProductionReport report) {
        ProductionReportDO DO = converter.toDO(report);
        mapper.insert(DO);
        report.setId(DO.getId());
    }

    @Override
    public Optional<ProductionReport> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id))
                .map(converter::toEntity);
    }

    @Override
    public Optional<ProductionReport> findByReportNo(String reportNo) {
        LambdaQueryWrapper<ProductionReportDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductionReportDO::getReportNo, reportNo);
        return Optional.ofNullable(mapper.selectOne(wrapper))
                .map(converter::toEntity);
    }

    @Override
    public List<ProductionReport> findByOrderId(Long orderId) {
        LambdaQueryWrapper<ProductionReportDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductionReportDO::getOrderId, orderId)
                .orderByDesc(ProductionReportDO::getReportTime);
        return mapper.selectList(wrapper).stream()
                .map(converter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionReport> findByTaskId(Long taskId) {
        LambdaQueryWrapper<ProductionReportDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductionReportDO::getTaskId, taskId)
                .orderByDesc(ProductionReportDO::getReportTime);
        return mapper.selectList(wrapper).stream()
                .map(converter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionReport> findByWorkerId(Long workerId) {
        LambdaQueryWrapper<ProductionReportDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductionReportDO::getWorkerId, workerId)
                .orderByDesc(ProductionReportDO::getReportTime);
        return mapper.selectList(wrapper).stream()
                .map(converter::toEntity)
                .collect(Collectors.toList());
    }
}