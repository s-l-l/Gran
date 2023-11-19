package com.shill.gran.common.user.service;

import com.shill.gran.common.framworkEntiry.enums.IdempotentEnum;
import com.shill.gran.common.user.domain.entity.UserBackpack;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户背包表 服务类
 * </p>
 *
 * @author shill
 * @since 2023-11-08
 */
public interface IUserBackpackService extends IService<UserBackpack> {

    /**
     * 用户获取一个物品
     *
     * @param uid            用户id
     * @param itemId         物品id
     * @param idempotentEnum 幂等类型
     * @param businessId     上层业务发送的唯一标识
     */
    void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId);
}
