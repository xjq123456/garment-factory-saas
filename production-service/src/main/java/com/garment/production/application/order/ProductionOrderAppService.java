package com.garment.production.application.order;

import com.garment.common.domain.BizException;
import com.garment.common.interfaces.PageResult;
import com.garment.production.application.order.dto.CreateProductionOrderCommand;
import com.garment.production.application.order.dto.ProductionOrderVO;
import com.garment.production.application.order.dto.UpdateProductionOrderCommand;
import com.garment.production.domain.order.entity.ProductionOrder;
import com.garment.production.domain.order.event.OrderCreatedEvent;
import com.garment.production.domain.order.event.OrderStatusChangedEvent;
import com.garment.production.domain.order.repository.ProductionOrderRepository;
import com.garment.production.domain.order.vo.OrderStatus;
import com.garment.production.domain.process.entity.ProcessRoute;
import com.garment.production.domain.process.entity.ProcessStep;
import com.garment.production.domain.process.repository.ProcessRouteRepository;
import com.garment.production.domain.task.entity.ProductionTask;
import com.garment.production.domain.task.repository.ProductionTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 生产工单应用服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductionOrderAppService {

    private final ProductionOrderRepository orderRepository;
    private final ProcessRouteRepository processRouteRepository;
    private final ProductionTaskRepository taskRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 创建生产工单
     */
    @Transactional
    public ProductionOrderVO createOrder(CreateProductionOrderCommand cmd) {
        // 生成工单编号
        String orderNo = generateOrderNo();

        // 创建工单
        ProductionOrder order = ProductionOrder.create(
                orderNo, cmd.getStyleId(), cmd.getStyleCode(),
                cmd.getStyleName(), cmd.getTotalQty(), cmd.getUnit()
        );
        order.setSkuId(cmd.getSkuId());
        order.setSkuCode(cmd.getSkuCode());
        order.setCustomerName(cmd.getCustomerName());
        order.setDeliveryDate(cmd.getDeliveryDate());
        order.setStartDate(cmd.getStartDate());
        order.setEndDate(cmd.getEndDate());
        order.setPriority(cmd.getPriority() != null ? cmd.getPriority() : 0);
        order.setRemark(cmd.getRemark());

        orderRepository.save(order);

        // 发布事件
        eventPublisher.publishEvent(new OrderCreatedEvent(
                order.getId(), orderNo, cmd.getStyleId(), cmd.getTotalQty()
        ));

        log.info("生产工单创建成功: orderNo={}, styleName={}, totalQty={}",
                orderNo, cmd.getStyleName(), cmd.getTotalQty());

        return toVO(order);
    }

    /**
     * 更新生产工单
     */
    @Transactional
    public ProductionOrderVO updateOrder(Long orderId, UpdateProductionOrderCommand cmd) {
        ProductionOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BizException("工单不存在: " + orderId));

        if (cmd.getCustomerName() != null) order.setCustomerName(cmd.getCustomerName());
        if (cmd.getTotalQty() != null) order.setTotalQty(cmd.getTotalQty());
        if (cmd.getUnit() != null) order.setUnit(cmd.getUnit());
        if (cmd.getDeliveryDate() != null) order.setDeliveryDate(cmd.getDeliveryDate());
        if (cmd.getStartDate() != null) order.setStartDate(cmd.getStartDate());
        if (cmd.getEndDate() != null) order.setEndDate(cmd.getEndDate());
        if (cmd.getPriority() != null) order.setPriority(cmd.getPriority());
        if (cmd.getRemark() != null) order.setRemark(cmd.getRemark());

        orderRepository.update(order);

        log.info("生产工单更新成功: orderId={}", orderId);
        return toVO(order);
    }

    /**
     * 审批工单
     */
    @Transactional
    public void approveOrder(Long orderId) {
        ProductionOrder order = getOrderOrThrow(orderId);
        OrderStatus oldStatus = order.getStatus();
        order.approve();
        orderRepository.update(order);

        eventPublisher.publishEvent(new OrderStatusChangedEvent(
                order.getId(), order.getOrderNo(), oldStatus, order.getStatus()
        ));

        log.info("工单审批通过: orderNo={}", order.getOrderNo());
    }

    /**
     * 开始生产 - 根据工艺路线自动拆分任务
     */
    @Transactional
    public void startProduction(Long orderId, Long routeId) {
        ProductionOrder order = getOrderOrThrow(orderId);

        // 加载工艺路线并拆分任务
        if (routeId != null) {
            ProcessRoute route = processRouteRepository.findById(routeId)
                    .orElseThrow(() -> new BizException("工艺路线不存在: " + routeId));

            order.setRouteId(routeId);
            // 为每个工序创建任务
            for (ProcessStep step : route.getSteps()) {
                String taskNo = generateTaskNo(order.getOrderNo(), step.getStepNo());
                ProductionTask task = ProductionTask.create(
                        taskNo, orderId, step.getId(), step.getStepName(), order.getTotalQty()
                );
                task.setRouteId(routeId);
                task.setPriority(order.getPriority());
                taskRepository.save(task);
            }
        }

        OrderStatus oldStatus = order.getStatus();
        order.startProduction();
        orderRepository.update(order);

        eventPublisher.publishEvent(new OrderStatusChangedEvent(
                order.getId(), order.getOrderNo(), oldStatus, order.getStatus()
        ));

        log.info("工单开始生产: orderNo={}, routeId={}", order.getOrderNo(), routeId);
    }

    /**
     * 暂停工单
     */
    @Transactional
    public void suspendOrder(Long orderId) {
        ProductionOrder order = getOrderOrThrow(orderId);
        OrderStatus oldStatus = order.getStatus();
        order.suspend();
        orderRepository.update(order);

        eventPublisher.publishEvent(new OrderStatusChangedEvent(
                order.getId(), order.getOrderNo(), oldStatus, order.getStatus()
        ));

        log.info("工单暂停: orderNo={}", order.getOrderNo());
    }

    /**
     * 恢复工单
     */
    @Transactional
    public void resumeOrder(Long orderId) {
        ProductionOrder order = getOrderOrThrow(orderId);
        OrderStatus oldStatus = order.getStatus();
        order.resume();
        orderRepository.update(order);

        eventPublisher.publishEvent(new OrderStatusChangedEvent(
                order.getId(), order.getOrderNo(), oldStatus, order.getStatus()
        ));

        log.info("工单恢复生产: orderNo={}", order.getOrderNo());
    }

    /**
     * 关闭工单
     */
    @Transactional
    public void closeOrder(Long orderId) {
        ProductionOrder order = getOrderOrThrow(orderId);
        OrderStatus oldStatus = order.getStatus();
        order.close();
        orderRepository.update(order);

        eventPublisher.publishEvent(new OrderStatusChangedEvent(
                order.getId(), order.getOrderNo(), oldStatus, order.getStatus()
        ));

        log.info("工单关闭: orderNo={}", order.getOrderNo());
    }

    /**
     * 查询工单详情
     */
    public ProductionOrderVO getOrder(Long orderId) {
        ProductionOrder order = getOrderOrThrow(orderId);
        return toVO(order);
    }

    /**
     * 分页查询工单列表
     */
    public PageResult<ProductionOrderVO> findOrders(String keyword, OrderStatus status,
                                                     Integer priority, int page, int size) {
        PageResult<ProductionOrder> pageResult = orderRepository.findPage(
                keyword, status, priority, page, size);

        List<ProductionOrderVO> voList = pageResult.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        return new PageResult<>(voList, pageResult.getTotal(), page, size);
    }

    /**
     * 查询逾期工单
     */
    public List<ProductionOrderVO> findOverdueOrders() {
        return orderRepository.findOverdueOrders().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    // ============ private methods ============

    private ProductionOrder getOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BizException("工单不存在: " + orderId));
    }

    private String generateOrderNo() {
        return "PO" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    private String generateTaskNo(String orderNo, int stepNo) {
        return orderNo + "-S" + String.format("%03d", stepNo);
    }

    private ProductionOrderVO toVO(ProductionOrder order) {
        ProductionOrderVO vo = new ProductionOrderVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setRouteId(order.getRouteId());
        vo.setStyleId(order.getStyleId());
        vo.setStyleCode(order.getStyleCode());
        vo.setStyleName(order.getStyleName());
        vo.setSkuId(order.getSkuId());
        vo.setSkuCode(order.getSkuCode());
        vo.setCustomerName(order.getCustomerName());
        vo.setTotalQty(order.getTotalQty());
        vo.setCompletedQty(order.getCompletedQty());
        vo.setDefectiveQty(order.getDefectiveQty());
        vo.setUnit(order.getUnit());
        vo.setDeliveryDate(order.getDeliveryDate());
        vo.setStartDate(order.getStartDate());
        vo.setEndDate(order.getEndDate());
        vo.setPriority(order.getPriority());
        vo.setStatus(order.getStatus());
        vo.setCompletionRate(order.getCompletionRate());
        vo.setOverdue(order.isOverdue());
        vo.setRemark(order.getRemark());
        vo.setCreateBy(String.valueOf(order.getCreateBy()));
        vo.setCreateTime(order.getCreateTime());
        vo.setUpdateBy(String.valueOf(order.getUpdateBy()));
        vo.setUpdateTime(order.getUpdateTime());
        return vo;
    }
}