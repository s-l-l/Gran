package com.shill.gran.common.user.dao;

import com.shill.gran.common.user.domain.entity.ItemConfig;
import com.shill.gran.common.user.mapper.ItemConfigMapper;
import com.shill.gran.common.user.service.IItemConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 功能物品配置表 服务实现类
 * </p>
 *
 * @author shill
 * @since 2023-11-08
 */
@Service
public class ItemConfigDao extends ServiceImpl<ItemConfigMapper, ItemConfig> implements IItemConfigService {
    public List<ItemConfig> getByType(Integer type) {
        return lambdaQuery().eq(ItemConfig::getType, type).list();
    }

}
