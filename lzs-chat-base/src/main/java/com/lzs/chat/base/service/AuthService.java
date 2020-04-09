package com.lzs.chat.base.service;


import com.lzs.chat.base.dto.req.AuthReqDto;
import com.lzs.chat.base.protobuf.Message;

/**
 * 用户验证接口
 */
public interface AuthService {

    /**
     * 用户身份验证
     * @param request
     * @param authReq
     * @return
     */
    Message.Response auth(Message.Request request, AuthReqDto authReq);


}
