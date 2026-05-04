package com.garment.production.infrastructure.persistence.workstation;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.garment.production.domain.workstation.entity.Workstation;
import com.garment.production.domain.workstation.repository.WorkstationRepository;
import com.garment.production.domain.workstation.vo.WorkstationStatus;
import com.garment.production.domain.workstation.vo.WorkstationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 工位仓储实现
 */
@Repository
@RequiredArgsConstructor
public class WorkstationRepositoryImpl implements WorkstationRepository {

    private final WorkstationMapper mapper;
    private final WorkstationConverter converter;

    @Override
    public void save(Workstation station) {
        WorkstationDO DO = converter.toDO(station);
        mapper.insert(DO);
        station.setId(DO.getId());
    }

    @Override
    public void update(Workstation station) {
        mapper.updateById(converter.toDO(station));
    }

    @Override
    public Optional<Workstation> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id))
                .map(converter::toEntity);
    }

    @Override
    public Optional<Workstation> findByStationCode(String stationCode) {
        LambdaQueryWrapper<WorkstationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WorkstationDO::getStationCode, stationCode);
        return Optional.ofNullable(mapper.selectOne(wrapper))
                .map(converter::toEntity);
    }

    @Override
    public List<Workstation> findAll(Long tenantId) {
        LambdaQueryWrapper<WorkstationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WorkstationDO::getTenantId, tenantId);
        return mapper.selectList(wrapper).stream()
                .map(converter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Workstation> findByStatus(Long tenantId, WorkstationStatus status) {
        LambdaQueryWrapper<WorkstationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WorkstationDO::getTenantId, tenantId)
                .eq(WorkstationDO::getStatus, status.getCode());
        return mapper.selectList(wrapper).stream()
                .map(converter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Workstation> findByType(Long tenantId, WorkstationType type) {
        LambdaQueryWrapper<WorkstationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WorkstationDO::getTenantId, tenantId)
                .eq(WorkstationDO::getStationType, type.getCode());
        return mapper.selectList(wrapper).stream()
                .map(converter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Workstation> findAvailable(Long tenantId, WorkstationType type) {
        LambdaQueryWrapper<WorkstationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WorkstationDO::getTenantId, tenantId)
                .eq(WorkstationDO::getStatus, WorkstationStatus.IDLE.getCode());
        if (type != null) {
            wrapper.eq(WorkstationDO::getStationType, type.getCode());
        }
        return mapper.selectList(wrapper).stream()
                .map(converter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Workstation> findByWorkshop(Long tenantId, String workshop) {
        LambdaQueryWrapper<WorkstationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WorkstationDO::getTenantId, tenantId)
                .eq(WorkstationDO::getWorkshop, workshop);
        return mapper.selectList(wrapper).stream()
                .map(converter::toEntity)
                .collect(Collectors.toList());
    }
}