package com.garment.production.infrastructure.persistence.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garment.common.interfaces.PageResult;
import com.garment.production.domain.task.entity.ProductionTask;
import com.garment.production.domain.task.repository.ProductionTaskRepository;
import com.garment.production.domain.task.vo.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 生产任务仓储实现
 */
@Repository
@RequiredArgsConstructor
public class ProductionTaskRepositoryImpl implements ProductionTaskRepository {

    private final ProductionTaskMapper mapper;
    private final ProductionTaskConverter converter;

    @Override
    public void save(ProductionTask task) {
        ProductionTaskDO DO = converter.toDO(task);
        mapper.insert(DO);
        task.setId(DO.getId());
    }

    @Override
    public void update(ProductionTask task) {
        mapper.updateById(converter.toDO(task));
    }

    @Override
    public Optional<ProductionTask> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id))
                .map(converter::toEntity);
    }

    @Override
    public Optional<ProductionTask> findByTaskNo(String taskNo) {
        LambdaQueryWrapper<ProductionTaskDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductionTaskDO::getTaskNo, taskNo);
        return Optional.ofNullable(mapper.selectOne(wrapper))
                .map(converter::toEntity);
    }

    @Override
    public List<ProductionTask> findByOrderId(Long orderId) {
        LambdaQueryWrapper<ProductionTaskDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductionTaskDO::getOrderId, orderId)
                .orderByAsc(ProductionTaskDO::getStepId);
        return mapper.selectList(wrapper).stream()
                .map(converter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionTask> findByStationId(Long stationId, TaskStatus status) {
        LambdaQueryWrapper<ProductionTaskDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductionTaskDO::getStationId, stationId);
        if (status != null) {
            wrapper.eq(ProductionTaskDO::getStatus, status.getCode());
        }
        return mapper.selectList(wrapper).stream()
                .map(converter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionTask> findByWorkerId(Long workerId, TaskStatus status) {
        LambdaQueryWrapper<ProductionTaskDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductionTaskDO::getWorkerId, workerId);
        if (status != null) {
            wrapper.eq(ProductionTaskDO::getStatus, status.getCode());
        }
        wrapper.orderByDesc(ProductionTaskDO::getCreateTime);
        return mapper.selectList(wrapper).stream()
                .map(converter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<ProductionTask> findPage(Long orderId, Long workerId,
                                                TaskStatus status, int page, int size) {
        LambdaQueryWrapper<ProductionTaskDO> wrapper = new LambdaQueryWrapper<>();
        if (orderId != null) {
            wrapper.eq(ProductionTaskDO::getOrderId, orderId);
        }
        if (workerId != null) {
            wrapper.eq(ProductionTaskDO::getWorkerId, workerId);
        }
        if (status != null) {
            wrapper.eq(ProductionTaskDO::getStatus, status.getCode());
        }
        wrapper.orderByDesc(ProductionTaskDO::getCreateTime);

        Page<ProductionTaskDO> pageResult = mapper.selectPage(new Page<>(page, size), wrapper);

        List<ProductionTask> records = pageResult.getRecords().stream()
                .map(converter::toEntity)
                .collect(Collectors.toList());

        return new PageResult<>(records, pageResult.getTotal(), page, size);
    }

    @Override
    public List<ProductionTask> findOverdueTasks() {
        LambdaQueryWrapper<ProductionTaskDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.lt(ProductionTaskDO::getPlanEndTime, LocalDateTime.now())
                .notIn(ProductionTaskDO::getStatus,
                        TaskStatus.COMPLETED.getCode(), TaskStatus.CANCELLED.getCode());
        return mapper.selectList(wrapper).stream()
                .map(converter::toEntity)
                .collect(Collectors.toList());
    }
}