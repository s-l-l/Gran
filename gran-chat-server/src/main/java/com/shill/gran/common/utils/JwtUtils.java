package com.shill.gran.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Description: jwt的token生成与解析
 * Author: <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-04-03
 */
@Slf4j
@Component
public class JwtUtils {
    /**
     * token秘钥，请勿泄露，请勿随便修改
     */
    //@Value("${mallchat.jwt.secret}")
    private static String secret = "shill";

    private static final String UID_CLAIM = "uid";
    private static final String CREATE_TIME = "createTime";

    /**
     * JWT生成Token.<br/>
     * <p>
     * JWT构成: header, payload, signature
     */
    public static String createToken(Long uid) {
        // build token
        String token = JWT.create()
                .withClaim(UID_CLAIM, uid) // 只存一个uid信息，其他的自己去redis查
                .withClaim(CREATE_TIME, new Date())
                .sign(Algorithm.HMAC256(secret)); // signature
        return token;
    }


    /**
     * 解密Token
     *
     * @param token
     * @return
     */
    public static Map<String, Claim> verifyToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaims();
        } catch (Exception e) {
            log.error("decode error,token:{}", token, e);
        }
        return null;
    }


    /**
     * 根据Token获取uid
     *
     * @param token
     * @return uid
     */
    public static Long getUidOrNull(String token) {
        return Optional.ofNullable(verifyToken(token))
                .map(map -> map.get(UID_CLAIM))
                .map(Claim::asLong)
                .orElse(null);
    }

}