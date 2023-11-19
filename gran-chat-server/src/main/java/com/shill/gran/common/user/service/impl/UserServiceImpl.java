package com.shill.gran.common.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shill.gran.common.user.dao.UserBackpackDao;
import com.shill.gran.common.user.domain.User;
import com.shill.gran.common.user.domain.vo.response.user.UserInfoResp;
import com.shill.gran.common.user.service.UserService;
import com.shill.gran.common.user.mapper.UserMapper;
import com.shill.gran.common.user.service.adapter.UserAdapter;
import lombok.Builder;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void modifyName(Long uid, String name) {
        //名字不能重复-查数据库看是否存在 -userDao

        //存在抛出异常-businessException

        //不存在修改
    }
}




