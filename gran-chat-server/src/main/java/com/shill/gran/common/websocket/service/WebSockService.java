package com.shill.gran.common.websocket.service;

import io.netty.channel.Channel;

/**
 * 用于保存用户的连接
 */
public interface WebSockService {

    void saveConnect(Channel channel);

    void handleLoginReq(Channel channel);

    void userOffLine(Channel channel);
}
