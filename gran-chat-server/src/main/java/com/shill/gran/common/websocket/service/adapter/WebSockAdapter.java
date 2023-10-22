package com.shill.gran.common.websocket.service.adapter;

import com.shill.gran.common.websocket.domain.WSLoginUrl;
import com.shill.gran.common.websocket.enums.WSBaseResp;
import com.shill.gran.common.websocket.enums.WSRespTypeEnum;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

public class WebSockAdapter {
    public static WSBaseResp<WSLoginUrl> buildResp(WxMpQrCodeTicket wxMpQrCodeTicket) {
        WSBaseResp<WSLoginUrl> baseResp = new WSBaseResp<>();
        baseResp.setType(WSRespTypeEnum.LOGIN_URL.getType());
        baseResp.setData(new WSLoginUrl(wxMpQrCodeTicket.getUrl()));
        return baseResp;
    }
}
