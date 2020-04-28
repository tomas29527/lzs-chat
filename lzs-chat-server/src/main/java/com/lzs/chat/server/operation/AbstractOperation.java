package com.lzs.chat.server.operation;

import com.alibaba.fastjson.JSON;
import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.base.dto.req.AuthReqDto;
import com.lzs.chat.base.protobuf.Message;
import com.lzs.chat.base.util.JWTUtil;
import com.lzs.chat.base.exception.AuthFailException;
import com.lzs.chat.base.exception.ParamErroException;
import com.lzs.chat.base.exception.TokenFailException;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 操作类抽象方法
 */
@Slf4j
public abstract class AbstractOperation implements Operation {

    protected String getKey(Channel ch) {
        return ch.attr(AppConstants.KEY_CONN_ID).get();
    }

    protected void setKey(Channel ch, String key) {
        ch.attr(AppConstants.KEY_CONN_ID).set(key);
    }


    /**
     * 认证参数验证
     * @param protocol
     * @throws ParamErroException
     */
    protected AuthReqDto authParamsCheck(Message.Protocol protocol) throws ParamErroException {
        try {
            AuthReqDto authReq = JSON.parseObject(protocol.getData(), AuthReqDto.class);
            if (Objects.isNull(authReq)) {
                log.error("====认证的参数错误,消息为空==== protocol:{}", protocol);
                throw new ParamErroException();
            }
            Integer roomId = authReq.getRoomId();
            if (Objects.isNull(roomId)) {
                log.error("====认证的参数错误,房间id不能为空==== protocol:{}", protocol);
                throw new ParamErroException();
            }
            return authReq;
        } catch (Exception e) {
            log.error("====认证的参数异常,消息为空==== protocol:{} error:", protocol, e);
            throw new ParamErroException();
        }
    }

    /**
     * appkey 验证
     *
     * @param protocol
     * @param platformKey
     * @return
     */
    protected void authAppKey(Message.Protocol protocol, String platformKey) throws AuthFailException {
        if (!platformKey.equals(protocol.getAppkey())) {
            throw new AuthFailException();
        }
    }

    /**
     * 验证token
     * @param protocol
     * @throws AuthFailException
     */
    protected void authToken(Message.Protocol protocol) throws TokenFailException {
        String token = protocol.getToken();
        if(StringUtils.isBlank(token)){
             throw new TokenFailException("未传token");
        }
        String verify = JWTUtil.verify(token);
        if(AppConstants.JWT_EXPIRED.equals(verify)){
            throw new TokenFailException("token失效");
        }else if(Objects.isNull(verify)){
            throw new TokenFailException("token不对");
        }
    }
}
