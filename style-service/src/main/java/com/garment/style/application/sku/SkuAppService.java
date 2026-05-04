package com.garment.style.application.sku;

import com.garment.common.domain.BizException;
import com.garment.common.domain.DomainEvent;
import com.garment.common.domain.TenantContext;
import com.garment.style.application.sku.dto.CreateSkuCommand;
import com.garment.style.application.sku.dto.UpdateSkuCommand;
import com.garment.style.domain.sku.entity.Sku;
import com.garment.style.domain.sku.repository.SkuRepository;
import com.garment.style.infrastructure.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkuAppService {

    private final SkuRepository skuRepository;
    private final DomainEventPublisher eventPublisher;

    @Transactional(rollbackFor = Exception.class)
    public Sku createSku(CreateSkuCommand cmd) {
        Long tenantId = TenantContext.requireTenantId();
        if (skuRepository.existsBySkuCode(cmd.getSkuCode(), tenantId)) {
            throw new BizException("SKU编码已存在: " + cmd.getSkuCode());
        }
        Sku sku = Sku.create(tenantId, cmd.getStyleId(), cmd.getSkuCode(),
                cmd.getColor(), cmd.getColorCode(), cmd.getSize(), cmd.getSizeType(),
                cmd.getBarcode(), cmd.getWeight(), cmd.getExtraPrice());
        skuRepository.save(sku);
        publishEvents(sku);
        return sku;
    }

    @Transactional(rollbackFor = Exception.class)
    public Sku updateSku(Long skuId, UpdateSkuCommand cmd) {
        Long tenantId = TenantContext.requireTenantId();
        Sku sku = skuRepository.findById(skuId, tenantId);
        if (sku == null) throw new BizException("SKU不存在: " + skuId);
        sku.update(cmd.getColor(), cmd.getColorCode(), cmd.getSize(), cmd.getSizeType(),
                cmd.getBarcode(), cmd.getWeight(), cmd.getExtraPrice());
        skuRepository.update(sku);
        publishEvents(sku);
        return sku;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteSku(Long skuId) {
        Long tenantId = TenantContext.requireTenantId();
        Sku sku = skuRepository.findById(skuId, tenantId);
        if (sku == null) throw new BizException("SKU不存在: " + skuId);
        skuRepository.deleteById(skuId, tenantId);
    }

    @Transactional(readOnly = true)
    public Sku getSku(Long skuId) {
        Long tenantId = TenantContext.requireTenantId();
        Sku sku = skuRepository.findById(skuId, tenantId);
        if (sku == null) throw new BizException("SKU不存在: " + skuId);
        return sku;
    }

    @Transactional(readOnly = true)
    public List<Sku> listByStyleId(Long styleId) {
        return skuRepository.findByStyleId(styleId, TenantContext.requireTenantId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void enableSku(Long skuId) {
        Long tenantId = TenantContext.requireTenantId();
        Sku sku = skuRepository.findById(skuId, tenantId);
        if (sku == null) throw new BizException("SKU不存在: " + skuId);
        sku.enable();
        skuRepository.update(sku);
    }

    @Transactional(rollbackFor = Exception.class)
    public void disableSku(Long skuId) {
        Long tenantId = TenantContext.requireTenantId();
        Sku sku = skuRepository.findById(skuId, tenantId);
        if (sku == null) throw new BizException("SKU不存在: " + skuId);
        sku.disable();
        skuRepository.update(sku);
    }

    private void publishEvents(Sku sku) {
        List<DomainEvent> events = sku.pullEvents();
        events.forEach(eventPublisher::publish);
    }
}
