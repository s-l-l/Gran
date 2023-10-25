package com.shill.gran.common.websocket.service.imp;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.shill.gran.common.user.domain.User;
import com.shill.gran.common.user.service.UserService;
import com.shill.gran.common.websocket.domain.WSLoginUrl;
import com.shill.gran.common.websocket.dto.WsChannelExtraDTO;
import com.shill.gran.common.websocket.enums.WSBaseResp;
import com.shill.gran.common.websocket.service.LoginService;
import com.shill.gran.common.websocket.service.WebSockService;
import com.shill.gran.common.websocket.service.adapter.WebSockAdapter;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Lazy
public class WebSockServiceImpl implements WebSockService {
    @Autowired
    @Lazy
    private WxMpService wxMpService;

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    private static final ConcurrentHashMap<Channel, WsChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();

    public static final int MAXIMUM_SIZE = 1000;

    public static final Duration DURATION = Duration.ofHours(1);

    private static final Cache<Integer, Channel> WAIT_LOGIN_MAP = Caffeine.newBuilder().maximumSize(MAXIMUM_SIZE)
            .expireAfterWrite(DURATION)
            .build();

    //设置二维码过期时间
    @Override
    public void saveConnect(Channel channel) {
        //ctrl+alt+c
        ONLINE_WS_MAP.put(channel, new WsChannelExtraDTO());
    }

    @Override
    public void waitAuthorize(Integer loginCode) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(loginCode);
        if (Objects.isNull(channel)) {
            throw new RuntimeException("信道被迫断开连接！请重新扫码登录");
        }
        sendMsg(channel, (WSBaseResp<WSLoginUrl>) WebSockAdapter.waitAuthorize());
    }


    @Override
    public void scanLoginSuccess(Integer loginCode, String id) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(loginCode);
        if (Objects.isNull(channel)) {
            throw new RuntimeException("信道被迫断开连接！请重新扫码登录");
        }
        List<User> userList = userService.lambdaQuery().eq(User::getOpenId, id).list();
        User user = null;
        if (!userList.isEmpty()) {
            user = userList.get(0);
        }
        //移除code
        WAIT_LOGIN_MAP.invalidate(loginCode);
        //调用登录获取token
        String token = loginService.login(Long.valueOf(id));
        sendMsg(channel, (WSBaseResp<WSLoginUrl>) WebSockAdapter.buildResp(user, token));
    }

    @Override
    public void userOffLine(Channel channel) {
        ONLINE_WS_MAP.remove(channel);
    }

    @SneakyThrows
    @Override
    public void handleLoginReq(Channel channel) {
        //生成随机码
        Integer code = generateLoginCode(channel);
        //带参二维码
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, (int) DURATION.getSeconds());
        //推送给前端
        sendMsg(channel, (WSBaseResp<WSLoginUrl>) WebSockAdapter.buildResp(wxMpQrCodeTicket));
    }

    private void sendMsg(Channel channel, WSBaseResp<WSLoginUrl> baseResp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(baseResp)));
    }


    private Integer generateLoginCode(Channel channel) {
        Integer code = null;
        do {
            code = RandomUtil.randomInt(Integer.MAX_VALUE);
        } while (Objects.nonNull(WAIT_LOGIN_MAP.asMap().putIfAbsent(code, channel)));
        return code;
    }
}
