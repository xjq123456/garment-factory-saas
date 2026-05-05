package com.garment.style.application.bom;

import com.garment.common.domain.AuthUserContext;
import com.garment.common.domain.BizException;
import com.garment.common.domain.DomainEvent;
import com.garment.style.application.bom.dto.CreateBomCommand;
import com.garment.style.application.bom.dto.UpdateBomCommand;
import com.garment.style.domain.bom.entity.Bom;
import com.garment.style.domain.bom.entity.BomItem;
import com.garment.style.domain.bom.event.BomCreatedEvent;
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
        Long tenantId = AuthUserContext.requireTenantId();
        if (bomRepository.existsByBomCode(cmd.getBomCode())) {
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
        eventPublisher.publish(new BomCreatedEvent(tenantId, bom.getId(), cmd.getBomCode()));
        return bom;
    }

    @Transactional(rollbackFor = Exception.class)
    public Bom updateBom(Long bomId, UpdateBomCommand cmd) {
        Long tenantId = AuthUserContext.requireTenantId();
        Bom bom = bomRepository.findById(bomId);
        if (bom == null) throw new BizException("BOM不存在: " + bomId);
        DomainEvent event = bom.update(cmd.getBomName(), cmd.getVersionNo(), cmd.getRemark());
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
        eventPublisher.publish(event);
        return bom;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBom(Long bomId) {
        Bom bom = bomRepository.findById(bomId);
        if (bom == null) throw new BizException("BOM不存在: " + bomId);
        bomRepository.deleteById(bomId);
    }

    @Transactional(readOnly = true)
    public Bom getBom(Long bomId) {
        Bom bom = bomRepository.findById(bomId);
        if (bom == null) throw new BizException("BOM不存在: " + bomId);
        return bom;
    }

    @Transactional(readOnly = true)
    public List<Bom> listByStyleId(Long styleId) {
        return bomRepository.findByStyleId(styleId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Bom confirmBom(Long bomId) {
        Bom bom = bomRepository.findById(bomId);
        if (bom == null) throw new BizException("BOM不存在: " + bomId);
        DomainEvent event = bom.confirm();
        bomRepository.update(bom);
        eventPublisher.publish(event);
        return bom;
    }

    @Transactional(rollbackFor = Exception.class)
    public Bom deprecateBom(Long bomId) {
        Bom bom = bomRepository.findById(bomId);
        if (bom == null) throw new BizException("BOM不存在: " + bomId);
        DomainEvent event = bom.deprecate();
        bomRepository.update(bom);
        eventPublisher.publish(event);
        return bom;
    }
}
