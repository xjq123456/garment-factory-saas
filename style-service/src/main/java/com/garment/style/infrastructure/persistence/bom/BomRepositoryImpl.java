package com.garment.style.infrastructure.persistence.bom;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.garment.style.domain.bom.entity.Bom;
import com.garment.style.domain.bom.entity.BomItem;
import com.garment.style.domain.bom.repository.BomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BomRepositoryImpl implements BomRepository {

    private final BomMapper bomMapper;
    private final BomItemMapper bomItemMapper;

    @Override
    public void save(Bom bom) {
        BomDO bomDO = BomConverter.toDO(bom);
        if (bomDO.getId() == null) {
            bomMapper.insert(bomDO);
            bom.setId(bomDO.getId());
        } else {
            bomMapper.updateById(bomDO);
        }
        // 保存明细行：先删后插
        if (bom.getItems() != null && !bom.getItems().isEmpty()) {
            deleteItemsByBomId(bom.getId(), bom.getTenantId());
            for (BomItem item : bom.getItems()) {
                item.setBomId(bom.getId());
                BomItemDO itemDO = BomConverter.itemToDO(item);
                bomItemMapper.insert(itemDO);
                item.setId(itemDO.getId());
            }
        }
    }

    @Override
    public void update(Bom bom) {
        bomMapper.updateById(BomConverter.toDO(bom));
    }

    @Override
    public Bom findById(Long id, Long tenantId) {
        LambdaQueryWrapper<BomDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BomDO::getId, id).eq(BomDO::getTenantId, tenantId);
        BomDO bomDO = bomMapper.selectOne(wrapper);
        if (bomDO == null) return null;
        Bom bom = BomConverter.toDomain(bomDO);
        // 加载明细
        LambdaQueryWrapper<BomItemDO> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(BomItemDO::getBomId, id).eq(BomItemDO::getTenantId, tenantId)
                   .orderByAsc(BomItemDO::getSortOrder);
        List<BomItemDO> itemDOs = bomItemMapper.selectList(itemWrapper);
        bom.setItems(BomConverter.itemsToDomain(itemDOs));
        return bom;
    }

    @Override
    public Bom findByCode(String bomCode, Long tenantId) {
        LambdaQueryWrapper<BomDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BomDO::getBomCode, bomCode).eq(BomDO::getTenantId, tenantId);
        BomDO bomDO = bomMapper.selectOne(wrapper);
        if (bomDO == null) return null;
        return BomConverter.toDomain(bomDO);
    }

    @Override
    public List<Bom> findByStyleId(Long styleId, Long tenantId) {
        LambdaQueryWrapper<BomDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BomDO::getStyleId, styleId).eq(BomDO::getTenantId, tenantId)
               .orderByDesc(BomDO::getCreatedAt);
        return bomMapper.selectList(wrapper).stream().map(BomConverter::toDomain).toList();
    }

    @Override
    public List<Bom> findByTenantId(Long tenantId) {
        LambdaQueryWrapper<BomDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BomDO::getTenantId, tenantId).orderByDesc(BomDO::getCreatedAt);
        return bomMapper.selectList(wrapper).stream().map(BomConverter::toDomain).toList();
    }

    @Override
    public void deleteById(Long id, Long tenantId) {
        deleteItemsByBomId(id, tenantId);
        LambdaQueryWrapper<BomDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BomDO::getId, id).eq(BomDO::getTenantId, tenantId);
        bomMapper.delete(wrapper);
    }

    @Override
    public void deleteByStyleId(Long styleId, Long tenantId) {
        // 先查出所有 BOM ID 再删明细
        LambdaQueryWrapper<BomDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BomDO::getStyleId, styleId).eq(BomDO::getTenantId, tenantId);
        List<BomDO> boms = bomMapper.selectList(wrapper);
        for (BomDO bom : boms) {
            deleteItemsByBomId(bom.getId(), tenantId);
        }
        bomMapper.delete(wrapper);
    }

    @Override
    public boolean existsByBomCode(String bomCode, Long tenantId) {
        LambdaQueryWrapper<BomDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BomDO::getBomCode, bomCode).eq(BomDO::getTenantId, tenantId);
        return bomMapper.selectCount(wrapper) > 0;
    }

    private void deleteItemsByBomId(Long bomId, Long tenantId) {
        LambdaQueryWrapper<BomItemDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BomItemDO::getBomId, bomId).eq(BomItemDO::getTenantId, tenantId);
        bomItemMapper.delete(wrapper);
    }
}
