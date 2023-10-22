package com.shill.gran.common.websocket.service;

import io.netty.channel.Channel;

/**
 * 用于保存用户的连接
 */
public interface WebSockService {
    /**
     * 保存连接
     * @param channel
     */
    void saveConnect(Channel channel);

    /**
     * 登录请求
     * @param channel
     */
    void handleLoginReq(Channel channel);

    /**
     * 用户下线
     * @param channel
     */
    void userOffLine(Channel channel);

    /**
     * 扫描登录
     * @param loginCode
     * @param id
     */
    void scanLoginSuccess(Integer loginCode, String id);

    /**
     * 等待授权
     * @param loginCode
     */
    void waitAuthorize(Integer loginCode);
}
