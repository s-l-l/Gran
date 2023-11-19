package com.shill.gran.common.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shill.gran.common.user.dao.ItemConfigDao;
import com.shill.gran.common.user.dao.UserBackpackDao;
import com.shill.gran.common.user.domain.User;
import com.shill.gran.common.user.domain.entity.ItemConfig;
import com.shill.gran.common.user.domain.entity.UserBackpack;
import com.shill.gran.common.user.domain.enums.ItemTypeEnum;
import com.shill.gran.common.user.domain.vo.response.user.BadgeResp;
import com.shill.gran.common.user.domain.vo.response.user.UserInfoResp;
import com.shill.gran.common.user.service.UserService;
import com.shill.gran.common.user.mapper.UserMapper;
import com.shill.gran.common.user.service.adapter.UserAdapter;
import com.shill.gran.common.user.service.cache.ItemCache;
import com.shill.gran.common.utils.AssertUtil;
import lombok.Builder;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2023-10-15 18:23:52
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Autowired
    private UserBackpackDao userBackpackDao;
    @Autowired
    private ItemConfigDao itemConfigDao;

    @Autowired
    private ItemCache itemCache;

    @Autowired
    private UserService userDao;


    @Override
    public User getById(Long uid) {
        User user = baseMapper.selectById(uid);
        return user;
    }

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User byId = this.getById(uid);
        Integer countByValidItemId = userBackpackDao.getCountByValidItemId(byId.getId(), byId.getItemId());
        UserInfoResp userInfoResp = UserAdapter.buildUserInfoResp(byId, countByValidItemId);
        return userInfoResp;
    }

    @Override
    @Transactional
    public void modifyName(Long uid, String name) {
        //名字不能重复-查数据库看是否存在 -userDao

        //存在抛出异常-businessException

        //不存在修改
        //获取第一张可以用的改名卡-如果没有改名卡了，抛出异常。

        //使用改名卡

        //修改用户名

    }

    @Override
    public List<BadgeResp> badges(Long uid) {
        //查询所有徽章
        List<ItemConfig> itemConfigs = itemCache.getByType(ItemTypeEnum.BADGE.getType());
        //查询用户拥有的徽章
        List<UserBackpack> backpacks = userBackpackDao.getByItemIds(uid, itemConfigs.stream().map(ItemConfig::getId).collect(Collectors.toList()));
        //查询用户当前佩戴的标签
        User user = userDao.getById(uid);
        return UserAdapter.buildBadgeResp(itemConfigs, backpacks, user);
    }
}




