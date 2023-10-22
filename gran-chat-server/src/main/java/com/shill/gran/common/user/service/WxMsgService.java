package com.shill.gran.common.user.service;

import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

public interface WxMsgService {
    /**
     * 微信扫码登录
     * @param wxMpService
     * @param wxMsg
     * @return
     */
    WxMpXmlOutMessage scan(WxMpService wxMpService, WxMpXmlMessage wxMsg);

    /**
     * 授权
     * @param userInfo
     */
    void authorize(WxOAuth2UserInfo userInfo);
}
