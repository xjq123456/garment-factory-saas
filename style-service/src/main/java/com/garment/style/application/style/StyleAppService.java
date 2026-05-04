package com.garment.style.application.style;

import com.garment.common.domain.BizException;
import com.garment.common.domain.DomainEvent;
import com.garment.common.domain.TenantContext;
import com.garment.common.interfaces.PageResult;
import com.garment.style.application.style.dto.CreateStyleCommand;
import com.garment.style.application.style.dto.UpdateStyleCommand;
import com.garment.style.domain.style.entity.Style;
import com.garment.style.domain.style.repository.StyleRepository;
import com.garment.style.infrastructure.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StyleAppService {

    private final StyleRepository styleRepository;
    private final DomainEventPublisher eventPublisher;

    @Transactional(rollbackFor = Exception.class)
    public Style createStyle(CreateStyleCommand cmd) {
        Long tenantId = TenantContext.requireTenantId();
        if (styleRepository.existsByStyleCode(cmd.getStyleCode(), tenantId)) {
            throw new BizException("款号已存在: " + cmd.getStyleCode());
        }
        Style style = Style.create(tenantId, cmd.getStyleCode(), cmd.getStyleName(),
                cmd.getCategoryId(), cmd.getSeason(), cmd.getYear(),
                cmd.getPatternType(), cmd.getCraftDesc(), cmd.getDesignSketch(),
                cmd.getMainImage(), cmd.getImages(), cmd.getTags(), cmd.getRemark());
        styleRepository.save(style);
        publishEvents(style);
        return style;
    }

    @Transactional(rollbackFor = Exception.class)
    public Style updateStyle(Long styleId, UpdateStyleCommand cmd) {
        Long tenantId = TenantContext.requireTenantId();
        Style style = styleRepository.findById(styleId, tenantId);
        if (style == null) throw new BizException("款式不存在: " + styleId);
        style.update(cmd.getStyleName(), cmd.getCategoryId(), cmd.getSeason(), cmd.getYear(),
                cmd.getPatternType(), cmd.getCraftDesc(), cmd.getDesignSketch(),
                cmd.getMainImage(), cmd.getImages(), cmd.getTags(), cmd.getRemark());
        styleRepository.update(style);
        publishEvents(style);
        return style;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteStyle(Long styleId) {
        Long tenantId = TenantContext.requireTenantId();
        Style style = styleRepository.findById(styleId, tenantId);
        if (style == null) throw new BizException("款式不存在: " + styleId);
        styleRepository.deleteById(styleId, tenantId);
    }

    @Transactional(readOnly = true)
    public Style getStyle(Long styleId) {
        Long tenantId = TenantContext.requireTenantId();
        Style style = styleRepository.findById(styleId, tenantId);
        if (style == null) throw new BizException("款式不存在: " + styleId);
        return style;
    }

    @Transactional(readOnly = true)
    public Style getStyleByCode(String styleCode) {
        Long tenantId = TenantContext.requireTenantId();
        Style style = styleRepository.findByCode(styleCode, tenantId);
        if (style == null) throw new BizException("款号不存在: " + styleCode);
        return style;
    }

    @Transactional(readOnly = true)
    public PageResult<Style> pageQuery(String keyword, Long categoryId, String season,
                                        Integer status, int pageNum, int pageSize) {
        return styleRepository.pageQuery(TenantContext.requireTenantId(),
                keyword, categoryId, season, status, pageNum, pageSize);
    }

    @Transactional(rollbackFor = Exception.class)
    public Style publishStyle(Long styleId) {
        Long tenantId = TenantContext.requireTenantId();
        Style style = styleRepository.findById(styleId, tenantId);
        if (style == null) throw new BizException("款式不存在: " + styleId);
        style.publish();
        styleRepository.update(style);
        publishEvents(style);
        return style;
    }

    @Transactional(rollbackFor = Exception.class)
    public Style deactivateStyle(Long styleId) {
        Long tenantId = TenantContext.requireTenantId();
        Style style = styleRepository.findById(styleId, tenantId);
        if (style == null) throw new BizException("款式不存在: " + styleId);
        style.deactivate();
        styleRepository.update(style);
        publishEvents(style);
        return style;
    }

    private void publishEvents(Style style) {
        List<DomainEvent> events = style.pullEvents();
        events.forEach(eventPublisher::publish);
    }
}
