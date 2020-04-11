package com.lzs.chat.base.service;


import com.lzs.chat.base.dto.req.AuthReqDto;
import com.lzs.chat.base.protobuf.Message;

/**
 * 用户验证接口
 */
public interface AuthService {

    /**
     * 用户身份验证
     * @param protocol
     * @param authReq
     * @return
     */
    Message.Protocol auth(Message.Protocol protocol, AuthReqDto authReq);


}
