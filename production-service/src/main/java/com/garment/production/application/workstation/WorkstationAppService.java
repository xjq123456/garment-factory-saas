package com.garment.production.application.workstation;

import com.garment.common.domain.BizException;
import com.garment.production.application.workstation.dto.CreateWorkstationCommand;
import com.garment.production.application.workstation.dto.WorkstationVO;
import com.garment.production.domain.workstation.entity.Workstation;
import com.garment.production.domain.workstation.repository.WorkstationRepository;
import com.garment.production.domain.workstation.vo.WorkstationStatus;
import com.garment.production.domain.workstation.vo.WorkstationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 工位管理应用服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkstationAppService {

    private final WorkstationRepository workstationRepository;

    /**
     * 创建工位
     */
    @Transactional
    public WorkstationVO createWorkstation(CreateWorkstationCommand cmd) {
        // 检查编号唯一性
        workstationRepository.findByStationCode(cmd.getStationCode())
                .ifPresent(s -> {
                    throw new BizException("工位编号已存在: " + cmd.getStationCode());
                });

        Workstation station = Workstation.create(
                cmd.getStationCode(), cmd.getStationName(),
                cmd.getWorkshop(), WorkstationType.valueOf(cmd.getStationType())
        );
        station.setProductionLine(cmd.getProductionLine());
        station.setEquipmentCode(cmd.getEquipmentCode());
        station.setRemark(cmd.getRemark());

        workstationRepository.save(station);

        log.info("工位创建成功: stationCode={}, stationName={}",
                cmd.getStationCode(), cmd.getStationName());

        return toVO(station);
    }

    /**
     * 绑定工人
     */
    @Transactional
    public void bindWorker(Long stationId, Long workerId, String workerName) {
        Workstation station = getStationOrThrow(stationId);
        station.bindWorker(workerId, workerName);
        workstationRepository.update(station);
        log.info("工位绑定工人: stationCode={}, workerName={}", station.getStationCode(), workerName);
    }

    /**
     * 解绑工人
     */
    @Transactional
    public void unbindWorker(Long stationId) {
        Workstation station = getStationOrThrow(stationId);
        station.unbindWorker();
        workstationRepository.update(station);
        log.info("工位解绑工人: stationCode={}", station.getStationCode());
    }

    /**
     * 停用工位
     */
    @Transactional
    public void disableStation(Long stationId) {
        Workstation station = getStationOrThrow(stationId);
        station.disable();
        workstationRepository.update(station);
        log.info("工位已停用: stationCode={}", station.getStationCode());
    }

    /**
     * 工位维护
     */
    @Transactional
    public void markMaintenance(Long stationId) {
        Workstation station = getStationOrThrow(stationId);
        station.markMaintenance();
        workstationRepository.update(station);
        log.info("工位进入维护: stationCode={}", station.getStationCode());
    }

    /**
     * 工位恢复空闲
     */
    @Transactional
    public void markIdle(Long stationId) {
        Workstation station = getStationOrThrow(stationId);
        station.markIdle();
        workstationRepository.update(station);
        log.info("工位恢复空闲: stationCode={}", station.getStationCode());
    }

    /**
     * 查询工位详情
     */
    public WorkstationVO getStation(Long stationId) {
        return toVO(getStationOrThrow(stationId));
    }

    /**
     * 查询所有工位
     */
    public List<WorkstationVO> listAllStations() {
        return workstationRepository.findAll().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    /**
     * 查询可用工位（指定类型）
     */
    public List<WorkstationVO> listAvailableStations(WorkstationType type) {
        return workstationRepository.findAvailable(type).stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    /**
     * 按车间查询工位
     */
    public List<WorkstationVO> listStationsByWorkshop(String workshop) {
        return workstationRepository.findByWorkshop(workshop).stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    // ============ private methods ============

    private Workstation getStationOrThrow(Long stationId) {
        return workstationRepository.findById(stationId)
                .orElseThrow(() -> new BizException("工位不存在: " + stationId));
    }

    private WorkstationVO toVO(Workstation station) {
        WorkstationVO vo = new WorkstationVO();
        vo.setId(station.getId());
        vo.setStationCode(station.getStationCode());
        vo.setStationName(station.getStationName());
        vo.setWorkshop(station.getWorkshop());
        vo.setProductionLine(station.getProductionLine());
        vo.setStationType(station.getStationType());
        vo.setWorkerId(station.getWorkerId());
        vo.setWorkerName(station.getWorkerName());
        vo.setEquipmentCode(station.getEquipmentCode());
        vo.setStatus(station.getStatus());
        vo.setRemark(station.getRemark());
        vo.setCreateTime(station.getCreateTime());
        vo.setUpdateTime(station.getUpdateTime());
        return vo;
    }
}