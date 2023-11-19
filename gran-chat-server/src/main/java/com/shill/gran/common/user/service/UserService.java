package com.shill.gran.common.user.service;

import com.shill.gran.common.user.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shill.gran.common.user.domain.vo.response.user.BadgeResp;
import com.shill.gran.common.user.domain.vo.response.user.UserInfoResp;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;

import java.util.List;

/**
* @author Administrator
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-10-15 18:23:52
*/
public interface UserService extends IService<User> {

    /**
     * 获得用户信息
     * @param uid
     * @return
     */
    User getById(Long uid);

    /**
     * 获取用户详细信息
     * @param uid
     * @return
     */
    UserInfoResp getUserInfo(Long uid);

    /**
     * 修改用户名
     * @param uid
     * @param name
     */
    void modifyName(Long uid, String name);

    /**
     * 可选徽章预览
     * @param uid
     * @return
     */
    List<BadgeResp> badges(Long uid);
}
