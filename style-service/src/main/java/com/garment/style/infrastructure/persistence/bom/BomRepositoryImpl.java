package com.garment.style.infrastructure.persistence.bom;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.garment.style.domain.bom.entity.Bom;
import com.garment.style.domain.bom.entity.BomItem;
import com.garment.style.domain.bom.repository.BomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * BOM 仓储实现。
 * <p>
 * 多租户隔离由 TenantLineInnerInterceptor 在 SQL 层自动追加 tenant_id 条件。
 */
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
            deleteItemsByBomId(bom.getId());
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
    public Bom findById(Long id) {
        BomDO bomDO = bomMapper.selectById(id);
        if (bomDO == null) return null;
        Bom bom = BomConverter.toDomain(bomDO);
        // 加载明细
        LambdaQueryWrapper<BomItemDO> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(BomItemDO::getBomId, id).orderByAsc(BomItemDO::getSortOrder);
        List<BomItemDO> itemDOs = bomItemMapper.selectList(itemWrapper);
        bom.setItems(BomConverter.itemsToDomain(itemDOs));
        return bom;
    }

    @Override
    public Bom findByCode(String bomCode) {
        LambdaQueryWrapper<BomDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BomDO::getBomCode, bomCode);
        BomDO bomDO = bomMapper.selectOne(wrapper);
        if (bomDO == null) return null;
        return BomConverter.toDomain(bomDO);
    }

    @Override
    public List<Bom> findByStyleId(Long styleId) {
        LambdaQueryWrapper<BomDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BomDO::getStyleId, styleId).orderByDesc(BomDO::getCreatedAt);
        return bomMapper.selectList(wrapper).stream().map(BomConverter::toDomain).toList();
    }

    @Override
    public List<Bom> findAll() {
        LambdaQueryWrapper<BomDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(BomDO::getCreatedAt);
        return bomMapper.selectList(wrapper).stream().map(BomConverter::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        deleteItemsByBomId(id);
        bomMapper.deleteById(id);
    }

    @Override
    public void deleteByStyleId(Long styleId) {
        // 先查出所有 BOM ID 再删明细
        LambdaQueryWrapper<BomDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BomDO::getStyleId, styleId);
        List<BomDO> boms = bomMapper.selectList(wrapper);
        for (BomDO bom : boms) {
            deleteItemsByBomId(bom.getId());
        }
        bomMapper.delete(wrapper);
    }

    @Override
    public boolean existsByBomCode(String bomCode) {
        LambdaQueryWrapper<BomDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BomDO::getBomCode, bomCode);
        return bomMapper.selectCount(wrapper) > 0;
    }

    private void deleteItemsByBomId(Long bomId) {
        LambdaQueryWrapper<BomItemDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BomItemDO::getBomId, bomId);
        bomItemMapper.delete(wrapper);
    }
}