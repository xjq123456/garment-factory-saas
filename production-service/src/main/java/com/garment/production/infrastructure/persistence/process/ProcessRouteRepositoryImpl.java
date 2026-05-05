package com.garment.production.infrastructure.persistence.process;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.garment.production.domain.process.entity.ProcessRoute;
import com.garment.production.domain.process.entity.ProcessStep;
import com.garment.production.domain.process.repository.ProcessRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 工艺路线仓储实现
 */
@Repository
@RequiredArgsConstructor
public class ProcessRouteRepositoryImpl implements ProcessRouteRepository {

    private final ProcessRouteMapper routeMapper;
    private final ProcessStepMapper stepMapper;
    private final ProcessRouteConverter routeConverter;
    private final ProcessStepConverter stepConverter;

    @Override
    @Transactional
    public void save(ProcessRoute route) {
        ProcessRouteDO routeDO = routeConverter.toDO(route);
        routeMapper.insert(routeDO);
        route.setId(routeDO.getId());

        // 保存工序步骤
        if (route.getSteps() != null) {
            for (ProcessStep step : route.getSteps()) {
                step.setRouteId(route.getId());
                ProcessStepDO stepDO = stepConverter.toDO(step);
                stepMapper.insert(stepDO);
                step.setId(stepDO.getId());
            }
        }
    }

    @Override
    @Transactional
    public void update(ProcessRoute route) {
        routeMapper.updateById(routeConverter.toDO(route));

        // 先删除旧步骤，再重新插入
        if (route.getSteps() != null) {
            LambdaQueryWrapper<ProcessStepDO> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(ProcessStepDO::getRouteId, route.getId());
            stepMapper.delete(deleteWrapper);

            for (ProcessStep step : route.getSteps()) {
                step.setRouteId(route.getId());
                ProcessStepDO stepDO = stepConverter.toDO(step);
                stepDO.setId(null); // 确保新增
                stepMapper.insert(stepDO);
                step.setId(stepDO.getId());
            }
        }
    }

    @Override
    public Optional<ProcessRoute> findById(Long id) {
        ProcessRouteDO routeDO = routeMapper.selectById(id);
        if (routeDO == null) return Optional.empty();

        ProcessRoute route = routeConverter.toEntity(routeDO);

        // 加载工序步骤
        LambdaQueryWrapper<ProcessStepDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessStepDO::getRouteId, id)
                .orderByAsc(ProcessStepDO::getStepNo);
        List<ProcessStep> steps = stepMapper.selectList(wrapper).stream()
                .map(stepConverter::toEntity)
                .collect(Collectors.toList());
        route.setSteps(steps);

        return Optional.of(route);
    }

    @Override
    public Optional<ProcessRoute> findByRouteCode(String routeCode) {
        LambdaQueryWrapper<ProcessRouteDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessRouteDO::getRouteCode, routeCode);
        return Optional.ofNullable(routeMapper.selectOne(wrapper))
                .map(routeConverter::toEntity);
    }

    @Override
    public List<ProcessRoute> findAll() {
        LambdaQueryWrapper<ProcessRouteDO> wrapper = new LambdaQueryWrapper<>();
        return routeMapper.selectList(wrapper).stream()
                .map(routeConverter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProcessRoute> findByStyleId(Long styleId) {
        LambdaQueryWrapper<ProcessRouteDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessRouteDO::getStyleId, styleId);
        return routeMapper.selectList(wrapper).stream()
                .map(routeConverter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProcessRoute> findByStatus(String status) {
        LambdaQueryWrapper<ProcessRouteDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessRouteDO::getStatus, status);
        return routeMapper.selectList(wrapper).stream()
                .map(routeConverter::toEntity)
                .collect(Collectors.toList());
    }
}