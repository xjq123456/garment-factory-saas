package com.garment.production.domain.process.repository;

import com.garment.production.domain.process.entity.ProcessRoute;

import java.util.List;
import java.util.Optional;

/**
 * 工艺路线仓储接口
 */
public interface ProcessRouteRepository {

    void save(ProcessRoute route);

    void update(ProcessRoute route);

    Optional<ProcessRoute> findById(Long id);

    Optional<ProcessRoute> findByRouteCode(String routeCode);

    List<ProcessRoute> findAll();

    List<ProcessRoute> findByStyleId(Long styleId);

    List<ProcessRoute> findByStatus(String status);
}