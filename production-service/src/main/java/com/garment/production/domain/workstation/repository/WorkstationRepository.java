package com.garment.production.domain.workstation.repository;

import com.garment.production.domain.workstation.entity.Workstation;
import com.garment.production.domain.workstation.vo.WorkstationStatus;
import com.garment.production.domain.workstation.vo.WorkstationType;

import java.util.List;
import java.util.Optional;

/**
 * 工位仓储接口
 */
public interface WorkstationRepository {

    void save(Workstation station);

    void update(Workstation station);

    Optional<Workstation> findById(Long id);

    Optional<Workstation> findByStationCode(String stationCode);

    List<Workstation> findAll(Long tenantId);

    List<Workstation> findByStatus(Long tenantId, WorkstationStatus status);

    List<Workstation> findByType(Long tenantId, WorkstationType type);

    List<Workstation> findAvailable(Long tenantId, WorkstationType type);

    List<Workstation> findByWorkshop(Long tenantId, String workshop);
}