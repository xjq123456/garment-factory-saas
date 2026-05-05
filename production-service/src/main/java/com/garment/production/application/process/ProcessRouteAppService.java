package com.garment.production.application.process;

import com.garment.common.domain.BizException;
import com.garment.production.application.process.dto.CreateProcessRouteCommand;
import com.garment.production.application.process.dto.ProcessRouteVO;
import com.garment.production.domain.process.entity.ProcessRoute;
import com.garment.production.domain.process.entity.ProcessStep;
import com.garment.production.domain.process.repository.ProcessRouteRepository;
import com.garment.production.domain.workstation.vo.WorkstationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 工艺路线应用服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessRouteAppService {

    private final ProcessRouteRepository processRouteRepository;

    /**
     * 创建工艺路线（含工序步骤）
     */
    @Transactional
    public ProcessRouteVO createRoute(CreateProcessRouteCommand cmd) {
        // 检查编号唯一性
        processRouteRepository.findByRouteCode(cmd.getRouteCode())
                .ifPresent(r -> {
                    throw new BizException("工艺路线编号已存在: " + cmd.getRouteCode());
                });

        ProcessRoute route = ProcessRoute.create(
                cmd.getRouteCode(), cmd.getRouteName(), cmd.getStyleId()
        );
        route.setDescription(cmd.getDescription());

        // 添加工序步骤
        if (cmd.getSteps() != null) {
            for (CreateProcessRouteCommand.ProcessStepDTO stepDTO : cmd.getSteps()) {
                ProcessStep step = ProcessStep.create(
                        stepDTO.getStepName(),
                        WorkstationType.valueOf(stepDTO.getStepType())
                );
                step.setStandardTime(stepDTO.getStandardTime());
                step.setStandardOutput(stepDTO.getStandardOutput());
                step.setDescription(stepDTO.getDescription());
                route.addStep(step);
            }
        }

        processRouteRepository.save(route);

        log.info("工艺路线创建成功: routeCode={}, routeName={}, steps={}",
                cmd.getRouteCode(), cmd.getRouteName(),
                route.getSteps().size());

        return toVO(route);
    }

    /**
     * 查询工艺路线详情
     */
    public ProcessRouteVO getRoute(Long routeId) {
        ProcessRoute route = processRouteRepository.findById(routeId)
                .orElseThrow(() -> new BizException("工艺路线不存在: " + routeId));
        return toVO(route);
    }

    /**
     * 查询所有启用的工艺路线
     */
    public List<ProcessRouteVO> listActiveRoutes() {
        return processRouteRepository.findByStatus("ACTIVE").stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    /**
     * 按款式查询工艺路线
     */
    public List<ProcessRouteVO> listRoutesByStyle(Long styleId) {
        return processRouteRepository.findByStyleId(styleId).stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    /**
     * 停用工艺路线
     */
    @Transactional
    public void deactivateRoute(Long routeId) {
        ProcessRoute route = processRouteRepository.findById(routeId)
                .orElseThrow(() -> new BizException("工艺路线不存在: " + routeId));
        route.setStatus("INACTIVE");
        processRouteRepository.update(route);
        log.info("工艺路线已停用: routeId={}", routeId);
    }

    /**
     * 启用工艺路线
     */
    @Transactional
    public void activateRoute(Long routeId) {
        ProcessRoute route = processRouteRepository.findById(routeId)
                .orElseThrow(() -> new BizException("工艺路线不存在: " + routeId));
        route.setStatus("ACTIVE");
        processRouteRepository.update(route);
        log.info("工艺路线已启用: routeId={}", routeId);
    }

    // ============ private methods ============

    private ProcessRouteVO toVO(ProcessRoute route) {
        ProcessRouteVO vo = new ProcessRouteVO();
        vo.setId(route.getId());
        vo.setRouteCode(route.getRouteCode());
        vo.setRouteName(route.getRouteName());
        vo.setStyleId(route.getStyleId());
        vo.setDescription(route.getDescription());
        vo.setStatus(route.getStatus());
        vo.setCreateBy(String.valueOf(route.getCreateBy()));
        vo.setCreateTime(route.getCreateTime());

        if (route.getSteps() != null) {
            vo.setSteps(route.getSteps().stream().map(step -> {
                ProcessRouteVO.ProcessStepVO stepVO = new ProcessRouteVO.ProcessStepVO();
                stepVO.setId(step.getId());
                stepVO.setStepNo(step.getStepNo());
                stepVO.setStepName(step.getStepName());
                stepVO.setStepType(step.getStepType() != null ? step.getStepType().getCode() : null);
                stepVO.setStandardTime(step.getStandardTime());
                stepVO.setStandardOutput(step.getStandardOutput());
                stepVO.setDescription(step.getDescription());
                return stepVO;
            }).collect(Collectors.toList()));
        }

        return vo;
    }
}