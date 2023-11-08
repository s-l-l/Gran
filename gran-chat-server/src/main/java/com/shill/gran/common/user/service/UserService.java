package com.shill.gran.common.user.service;

import com.shill.gran.common.user.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;

/**
* @author Administrator
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-10-15 18:23:52
*/
public interface UserService extends IService<User> {

    User getById(Long uid);

}
