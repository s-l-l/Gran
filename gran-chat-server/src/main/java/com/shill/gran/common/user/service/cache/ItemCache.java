package com.shill.gran.common.user.service.cache;

import com.shill.gran.common.user.dao.ItemConfigDao;
import com.shill.gran.common.user.domain.entity.ItemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemCache {
    @Autowired
    private ItemConfigDao itemConfigDao;

    @Cacheable(cacheNames = "item",key = "'itemByType:'+#type")
    public List<ItemConfig> getByType(Integer type) {
        return itemConfigDao.getByType(type);
    }

    @Cacheable(cacheNames = "item",key = "'item:'+#itemId")
    public ItemConfig getById(Long itemId) {
        return itemConfigDao.getById(itemId);
    }

}
