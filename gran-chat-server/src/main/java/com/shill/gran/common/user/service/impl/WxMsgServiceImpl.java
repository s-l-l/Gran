package com.shill.gran.common.user.service.impl;

import com.shill.gran.common.user.domain.User;
import com.shill.gran.common.user.service.UserService;
import com.shill.gran.common.user.service.WxMsgService;
import com.shill.gran.common.websocket.service.WebSockService;
import com.shill.gran.common.websocket.service.adapter.TextBuilder;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class WxMsgServiceImpl implements WxMsgService {
    @Autowired
    private UserService userService;
    @Autowired
    private WebSockService webSockService;
    private static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    //openid和登录code的关系map
    private static final ConcurrentHashMap<String,Integer> WAITE_AUTHORIZE_MAP = new ConcurrentHashMap<>();

    @Value("${wx.mp.callback}")
    private String callback;

    @Override
    public WxMpXmlOutMessage scan(WxMpService wxMpService, WxMpXmlMessage wxMsg) {
        Integer loginCode = Integer.parseInt(getEventKey(wxMsg));
        //保存用户信息
        String openId = wxMsg.getFromUser();
        List<User> userList = userService.lambdaQuery().eq(User::getOpenId, wxMsg.getFromUser()).list();
        User user;
        //为空保存用户信息
        if (userList.isEmpty()) {
            user = User.builder().openId(wxMsg.getFromUser()).build();
            userService.save(user);
        }
        //不为空
        if (!userList.isEmpty()) {
            User user1 = userList.get(0);
            if (StringUtils.isNotEmpty(user1.getAvatar())) {
                WAITE_AUTHORIZE_MAP.put(wxMsg.getOpenId(), loginCode);
            }
            //调用登录成功的逻辑
            webSockService.scanLoginSuccess(loginCode,user1.getOpenId());
            return null;
        }
        //告诉用户正在等待中。
        webSockService.waitAuthorize(loginCode);
        String skipUrl = String.format(URL, wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode(callback + "/wx/portal/public/callBack"));
        return new TextBuilder().build("请点击链接授权：<a href=\"" + skipUrl + "\">登录</a>", wxMsg, wxMpService);
    }

    @Override
    public void authorize(WxOAuth2UserInfo userInfo) {
        List<User> userList = userService.lambdaQuery().eq(User::getOpenId, userInfo.getOpenid()).list();
        if (!userList.isEmpty()) {
            User user = userList.get(0);
           user.setAvatar(userInfo.getHeadImgUrl());
           user.setName(userInfo.getNickname());
           user.setId(user.getId());
           userService.update(user,null);
            WAITE_AUTHORIZE_MAP.remove(userInfo.getOpenid());

        }
    }

    private String getEventKey(WxMpXmlMessage wxMpXmlMessage) {
        //扫码关注的渠道事件有前缀，需要去除
        return wxMpXmlMessage.getEventKey().replace("qrscene_", "");
    }
}
