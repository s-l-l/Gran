package com.shill.gran.common.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shill.gran.common.user.dao.UserBackpackDao;
import com.shill.gran.common.user.domain.User;
import com.shill.gran.common.user.domain.vo.response.user.UserInfoResp;
import com.shill.gran.common.user.service.UserService;
import com.shill.gran.common.user.mapper.UserMapper;
import com.shill.gran.common.user.service.adapter.UserAdapter;
import com.shill.gran.common.utils.AssertUtil;
import lombok.Builder;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;

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

    @Autowired
    private ItemConfigDao itemConfigDao;


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
        try {
            //名字不能重复-查数据库看是否存在 -userDao
            User user = this.getOne(new QueryWrapper<User>().eq("name", name));
            if (user != null) {
                //存在抛出异常-businessException
                throw new BusinessException();
            } else {
                //不存在修改
                ItemConfig itemConfigId = itemConfigDao.getOne(new QueryWrapper<ItemConfig>().eq("type", 1));
                List<UserBackpack> byIds = userBackpackDao.list(new QueryWrapper<UserBackpack>().eq("uid", uid).eq("status", 0).eq("item_id", itemConfigId));
                if (byIds == null) {
                    //获取第一张可以用的改名卡-如果没有改名卡了，抛出异常。
                    throw new BusinessException();
                } else {
                    //使用改名卡
                    Long userBackpackId = byIds.get(0).getId();
                    userBackpackDao.update().eq("id", userBackpackId).set("status", 1);
                    //修改用户名
                    this.update().eq("id", uid).set("name", name);
                }
            }
        } catch (Exception e) {
            Console.log(e);
        }

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




