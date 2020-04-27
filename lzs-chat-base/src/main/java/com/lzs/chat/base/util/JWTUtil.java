package com.lzs.chat.base.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lzs.chat.base.constans.AppConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/27 16:28
 * @since <版本号>
 */
@Slf4j
public class JWTUtil {
    //过期时间(1天)
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;
    //私钥
    private static final String TOKEN_SECRET = "lzstokencreateWXSE1";

    /**
     * 根据userId 生成token
     *
     * @param userId
     * @return
     */
    public static String createToken(String userId) {
        try {
            // 设置过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            // 私钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("Type", "Jwt");
            header.put("alg", "HS256");
            // 返回token字符串
            return JWT.create()
                    .withHeader(header)
                    .withClaim("userId", userId)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            log.error("生成token 异常==== error :", e);
            return null;
        }

    }

    /**
     * 检验token是否正确
     *
     * @param **token**
     * @return
     */
    public static String verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            String userId = jwt.getClaim("userId").asString();
            return userId;
        } catch (Exception e) {
            log.error("检验token异常==== error :", e);
            if (e instanceof TokenExpiredException) {
                //过期
                return AppConstants.JWT_EXPIRED;
            }
            return null;
        }
    }
}
