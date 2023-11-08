package com.shill.gran.common.websocket.service.adapter;

import com.shill.gran.common.user.domain.User;
import com.shill.gran.common.websocket.domain.WSLoginSuccess;
import com.shill.gran.common.websocket.domain.WSLoginUrl;
import com.shill.gran.common.websocket.enums.WSBaseResp;
import com.shill.gran.common.websocket.enums.WSRespTypeEnum;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

public class WebSockAdapter {
    public static WSBaseResp<?> buildResp(WxMpQrCodeTicket wxMpQrCodeTicket) {
        WSBaseResp<WSLoginUrl> baseResp = new WSBaseResp<>();
        baseResp.setType(WSRespTypeEnum.LOGIN_URL.getType());
        baseResp.setData(new WSLoginUrl(wxMpQrCodeTicket.getUrl()));
        return baseResp;
    }

    public static WSBaseResp<?> buildResp(User user, String token) {
        WSBaseResp<WSLoginSuccess> baseResp = new WSBaseResp<>();
        baseResp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        WSLoginSuccess build = WSLoginSuccess.builder().avatar(user.getAvatar())
                .name(user.getName())
                .token(token)
                .uid(user.getId()).build();
        baseResp.setData(build);
        return baseResp;
    }

    public static WSBaseResp<?> waitAuthorize() {
        WSBaseResp<WSLoginUrl> baseResp = new WSBaseResp<>();
        baseResp.setType(WSRespTypeEnum.LOGIN_SCAN_SUCCESS.getType());
        return baseResp;
    }
    //失效重新登录
    public static WSBaseResp<?> buildInvalidTokenResp() {
        WSBaseResp<WSLoginUrl> baseResp = new WSBaseResp<>();
        baseResp.setType(WSRespTypeEnum.INVALIDATE_TOKEN.getType());
        return baseResp;
    }
}
