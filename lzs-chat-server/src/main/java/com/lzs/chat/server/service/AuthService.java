package com.lzs.chat.server.service;


import com.lzs.chat.base.dto.req.AuthReqDto;
import com.lzs.chat.base.protobuf.Message;
import io.netty.channel.Channel;

/**
 * 用户验证接口
 */
public interface AuthService {

    /**
     * 连接认证
     * @param protocol
     * @param authReq
     * @param ch
     * @return String 返回 token 返回null 就是认证失败
     */
    public void auth(Message.Protocol protocol, AuthReqDto authReq, Channel ch) ;


}
