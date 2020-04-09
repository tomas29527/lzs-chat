package com.lzs.chat.base.service;


import com.lzs.chat.base.protobuf.Message;

/**
 * 消息处理接口
 */
public interface MsgService {

    /**
     * 接收消息
     *
     * @param request 协议
     * @return 是否处理成功
     */
    Message.Response receive(Message.Request request);
}
