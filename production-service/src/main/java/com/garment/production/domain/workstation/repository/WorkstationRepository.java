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

    List<Workstation> findAll();

    List<Workstation> findByStatus(WorkstationStatus status);

    List<Workstation> findByType(WorkstationType type);

    List<Workstation> findAvailable(WorkstationType type);

    List<Workstation> findByWorkshop(String workshop);
}