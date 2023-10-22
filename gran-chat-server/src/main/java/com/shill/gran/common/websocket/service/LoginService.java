package com.shill.gran.common.websocket.service;

public interface LoginService {
    /**
     * 生成Token
     * @param id
     * @return
     */
    String login(String id);
}
