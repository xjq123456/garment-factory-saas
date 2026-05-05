package com.garment.style.application.style;

import com.garment.common.domain.AuthUserContext;
import com.garment.common.domain.BizException;
import com.garment.common.domain.DomainEvent;
import com.garment.common.interfaces.PageResult;
import com.garment.style.application.style.dto.CreateStyleCommand;
import com.garment.style.application.style.dto.UpdateStyleCommand;
import com.garment.style.domain.style.entity.Style;
import com.garment.style.domain.style.event.StyleCreatedEvent;
import com.garment.style.domain.style.repository.StyleRepository;
import com.garment.style.infrastructure.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StyleAppService {

    private final StyleRepository styleRepository;
    private final DomainEventPublisher eventPublisher;

    @Transactional(rollbackFor = Exception.class)
    public Style createStyle(CreateStyleCommand cmd) {
        Long tenantId = AuthUserContext.requireTenantId();
        if (styleRepository.existsByStyleCode(cmd.getStyleCode())) {
            throw new BizException("款号已存在: " + cmd.getStyleCode());
        }
        Style style = Style.create(tenantId, cmd.getStyleCode(), cmd.getStyleName(),
                cmd.getCategoryId(), cmd.getSeason(), cmd.getYear(),
                cmd.getPatternType(), cmd.getCraftDesc(), cmd.getDesignSketch(),
                cmd.getMainImage(), cmd.getImages(), cmd.getTags(), cmd.getRemark());
        styleRepository.save(style);
        eventPublisher.publish(new StyleCreatedEvent(tenantId, cmd.getStyleCode(), cmd.getStyleName()));
        return style;
    }

    @Transactional(rollbackFor = Exception.class)
    public Style updateStyle(Long styleId, UpdateStyleCommand cmd) {
        Style style = styleRepository.findById(styleId);
        if (style == null) throw new BizException("款式不存在: " + styleId);
        DomainEvent event = style.update(cmd.getStyleName(), cmd.getCategoryId(), cmd.getSeason(), cmd.getYear(),
                cmd.getPatternType(), cmd.getCraftDesc(), cmd.getDesignSketch(),
                cmd.getMainImage(), cmd.getImages(), cmd.getTags(), cmd.getRemark());
        styleRepository.update(style);
        eventPublisher.publish(event);
        return style;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteStyle(Long styleId) {
        Style style = styleRepository.findById(styleId);
        if (style == null) throw new BizException("款式不存在: " + styleId);
        styleRepository.deleteById(styleId);
    }

    @Transactional(readOnly = true)
    public Style getStyle(Long styleId) {
        Style style = styleRepository.findById(styleId);
        if (style == null) throw new BizException("款式不存在: " + styleId);
        return style;
    }

    @Transactional(readOnly = true)
    public Style getStyleByCode(String styleCode) {
        Style style = styleRepository.findByCode(styleCode);
        if (style == null) throw new BizException("款号不存在: " + styleCode);
        return style;
    }

    @Transactional(readOnly = true)
    public PageResult<Style> pageQuery(String keyword, Long categoryId, String season,
                                        Integer status, int pageNum, int pageSize) {
        return styleRepository.pageQuery(keyword, categoryId, season, status, pageNum, pageSize);
    }

    @Transactional(rollbackFor = Exception.class)
    public Style publishStyle(Long styleId) {
        Style style = styleRepository.findById(styleId);
        if (style == null) throw new BizException("款式不存在: " + styleId);
        DomainEvent event = style.publish();
        styleRepository.update(style);
        eventPublisher.publish(event);
        return style;
    }

    @Transactional(rollbackFor = Exception.class)
    public Style deactivateStyle(Long styleId) {
        Style style = styleRepository.findById(styleId);
        if (style == null) throw new BizException("款式不存在: " + styleId);
        DomainEvent event = style.deactivate();
        styleRepository.update(style);
        eventPublisher.publish(event);
        return style;
    }
}
