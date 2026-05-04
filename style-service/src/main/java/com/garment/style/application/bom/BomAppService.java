package com.garment.style.application.bom;

import com.garment.common.domain.BizException;
import com.garment.common.domain.DomainEvent;
import com.garment.common.domain.TenantContext;
import com.garment.style.application.bom.dto.CreateBomCommand;
import com.garment.style.application.bom.dto.UpdateBomCommand;
import com.garment.style.domain.bom.entity.Bom;
import com.garment.style.domain.bom.entity.BomItem;
import com.garment.style.domain.bom.repository.BomRepository;
import com.garment.style.infrastructure.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BomAppService {

    private final BomRepository bomRepository;
    private final DomainEventPublisher eventPublisher;

    @Transactional(rollbackFor = Exception.class)
    public Bom createBom(CreateBomCommand cmd) {
        Long tenantId = TenantContext.requireTenantId();
        if (bomRepository.existsByBomCode(cmd.getBomCode(), tenantId)) {
            throw new BizException("BOM编码已存在: " + cmd.getBomCode());
        }
        Bom bom = Bom.create(tenantId, cmd.getStyleId(), cmd.getBomCode(),
                cmd.getBomName(), cmd.getVersionNo(), cmd.getRemark());
        if (cmd.getItems() != null) {
            List<BomItem> items = new ArrayList<>();
            for (CreateBomCommand.BomItemDTO itemDTO : cmd.getItems()) {
                BomItem item = BomItem.create(tenantId, null, itemDTO.getMaterialName(),
                        itemDTO.getMaterialCode(), itemDTO.getMaterialType(),
                        itemDTO.getSpecification(), itemDTO.getUnit(),
                        itemDTO.getQuantity(), itemDTO.getUnitPrice(),
                        itemDTO.getSupplier(), itemDTO.getColor(),
                        itemDTO.getRemark(), itemDTO.getSortOrder());
                items.add(item);
            }
            bom.setItems(items);
        }
        bomRepository.save(bom);
        publishEvents(bom);
        return bom;
    }

    @Transactional(rollbackFor = Exception.class)
    public Bom updateBom(Long bomId, UpdateBomCommand cmd) {
        Long tenantId = TenantContext.requireTenantId();
        Bom bom = bomRepository.findById(bomId, tenantId);
        if (bom == null) throw new BizException("BOM不存在: " + bomId);
        bom.update(cmd.getBomName(), cmd.getVersionNo(), cmd.getRemark());
        if (cmd.getItems() != null) {
            List<BomItem> items = new ArrayList<>();
            for (CreateBomCommand.BomItemDTO itemDTO : cmd.getItems()) {
                BomItem item = BomItem.create(tenantId, null, itemDTO.getMaterialName(),
                        itemDTO.getMaterialCode(), itemDTO.getMaterialType(),
                        itemDTO.getSpecification(), itemDTO.getUnit(),
                        itemDTO.getQuantity(), itemDTO.getUnitPrice(),
                        itemDTO.getSupplier(), itemDTO.getColor(),
                        itemDTO.getRemark(), itemDTO.getSortOrder());
                items.add(item);
            }
            bom.setItems(items);
        }
        bomRepository.save(bom);
        return bom;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBom(Long bomId) {
        Long tenantId = TenantContext.requireTenantId();
        Bom bom = bomRepository.findById(bomId, tenantId);
        if (bom == null) throw new BizException("BOM不存在: " + bomId);
        bomRepository.deleteById(bomId, tenantId);
    }

    @Transactional(readOnly = true)
    public Bom getBom(Long bomId) {
        Long tenantId = TenantContext.requireTenantId();
        Bom bom = bomRepository.findById(bomId, tenantId);
        if (bom == null) throw new BizException("BOM不存在: " + bomId);
        return bom;
    }

    @Transactional(readOnly = true)
    public List<Bom> listByStyleId(Long styleId) {
        return bomRepository.findByStyleId(styleId, TenantContext.requireTenantId());
    }

    @Transactional(rollbackFor = Exception.class)
    public Bom confirmBom(Long bomId) {
        Long tenantId = TenantContext.requireTenantId();
        Bom bom = bomRepository.findById(bomId, tenantId);
        if (bom == null) throw new BizException("BOM不存在: " + bomId);
        bom.confirm();
        bomRepository.update(bom);
        publishEvents(bom);
        return bom;
    }

    @Transactional(rollbackFor = Exception.class)
    public Bom deprecateBom(Long bomId) {
        Long tenantId = TenantContext.requireTenantId();
        Bom bom = bomRepository.findById(bomId, tenantId);
        if (bom == null) throw new BizException("BOM不存在: " + bomId);
        bom.deprecate();
        bomRepository.update(bom);
        publishEvents(bom);
        return bom;
    }

    private void publishEvents(Bom bom) {
        List<DomainEvent> events = bom.pullEvents();
        events.forEach(eventPublisher::publish);
    }
}
