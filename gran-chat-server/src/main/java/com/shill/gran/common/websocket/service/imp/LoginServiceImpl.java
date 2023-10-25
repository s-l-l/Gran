package com.shill.gran.common.websocket.service.imp;

import com.shill.gran.common.constant.RedisKey;
import com.shill.gran.common.utils.JwtUtils;
import com.shill.gran.common.utils.RedisUtils;
import com.shill.gran.common.websocket.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.shill.gran.common.constant.RedisKey.USER_TOKEN_STRING;

@Service
public class LoginServiceImpl implements LoginService {

    public static final int TIEMOUT = 3;

    @Override
    public boolean verify(String token) {
        return false;
    }

    @Override
    public void renewalTokenIfNecessary(String token) {

    }

    @Override
    public String login(Long id) {
        String token = JwtUtils.createToken(id);
        //使用redis存起来！
        RedisUtils.set(RedisKey.getKey(USER_TOKEN_STRING, id), token, TIEMOUT, TimeUnit.DAYS);
        return token;
    }

    @Override
    public Long getValidUid(String token) {
        Long uidOrNull = JwtUtils.getUidOrNull(token);
        if (Objects.isNull(uidOrNull)) {
            return null;
        }
        String uid = RedisUtils.get(RedisKey.getKey(getUserTokenKey(uidOrNull)));
        if (StringUtils.isBlank(uid)) {
            return null;
        }
        return uidOrNull;
    }

    public String getUserTokenKey(Long id) {
        return RedisKey.getKey(USER_TOKEN_STRING, id);
    }
}
