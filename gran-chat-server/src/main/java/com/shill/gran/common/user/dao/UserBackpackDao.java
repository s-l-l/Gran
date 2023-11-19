package com.shill.gran.common.user.dao;

import com.shill.gran.common.framworkEntiry.enums.IdempotentEnum;
import com.shill.gran.common.framworkEntiry.enums.YesOrNoEnum;
import com.shill.gran.common.user.domain.entity.ItemConfig;
import com.shill.gran.common.user.domain.entity.UserBackpack;
import com.shill.gran.common.user.domain.enums.ItemTypeEnum;
import com.shill.gran.common.user.mapper.UserBackpackMapper;
import com.shill.gran.common.user.service.IUserBackpackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shill.gran.common.user.service.cache.ItemCache;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 用户背包表 服务实现类
 * </p>
 *
 * @author shill
 * @since 2023-11-08
 */
@Service
public class UserBackpackDao extends ServiceImpl<UserBackpackMapper, UserBackpack> implements IUserBackpackService {
    @Autowired
    private UserBackpackDao userBackpackDao;
    @Autowired
    private ItemCache itemCache;

    @Autowired
    private RedissonClient redissonClient;

    public Integer getCountByValidItemId(Long uid,Long itemId){
        return lambdaQuery().eq(UserBackpack::getUid,uid)
                .eq(UserBackpack::getItemId,itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus()).count();
    }

    /**
     * 拥有的
     * @param uid
     * @param itemIds
     * @return
     */
    public List<UserBackpack> getByItemIds(Long uid, List<Long> itemIds) {
        return lambdaQuery().eq(UserBackpack::getUid, uid)
                .in(UserBackpack::getItemId, itemIds)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .list();
    }

    public List<UserBackpack> getByItemIds(List<Long> uids, List<Long> itemIds) {
        return lambdaQuery().in(UserBackpack::getUid, uids)
                .in(UserBackpack::getItemId, itemIds)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .list();
    }

    public UserBackpack getByIdp(String idempotent) {
        return lambdaQuery().eq(UserBackpack::getIdempotent, idempotent).one();
    }

    @Override
    public void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        //组装幂等号
        String idempotent = getIdempotent(itemId, idempotentEnum, businessId);
        this.doAcquireItem(uid, itemId, idempotent);
    }
    private void doAcquireItem(Long uid, Long itemId, String idempotent) {
        RLock lock = redissonClient.getLock(idempotent);
        lock.tryLock();
        try {
            UserBackpack userBackpack = userBackpackDao.getByIdp(idempotent);
            //幂等检查
            if (Objects.nonNull(userBackpack)) {
                return;
            }
            //业务检查
            ItemConfig itemConfig = itemCache.getById(itemId);
            if (ItemTypeEnum.BADGE.getType().equals(itemConfig.getType())) {//徽章类型做唯一性检查
                Integer countByValidItemId = userBackpackDao.getCountByValidItemId(uid, itemId);
                if (countByValidItemId > 0) {//已经有徽章了不发
                    return;
                }
            }
            //发物品
            UserBackpack insert = UserBackpack.builder()
                    .uid(uid)
                    .itemId(itemId)
                    .status(YesOrNoEnum.NO.getStatus())
                    .idempotent(idempotent)
                    .build();
            userBackpackDao.save(insert);
        }catch (Exception e){
            lock.unlock();
        }

    }

    private String getIdempotent(Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        return String.format("%d_%d_%s", itemId, idempotentEnum.getType(), businessId);
    }
}
