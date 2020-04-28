package com.lzs.chat.service.service;


import com.lzs.chat.base.protobuf.Message;
import io.netty.channel.Channel;

/**
 * 消息处理接口
 */
public interface MsgService {

    /**
     * 发送消息给其他用户
     * @param protocol 协议
     * @return 是否处理成功
     */
    Message.Protocol sendMsgToOther(Message.Protocol protocol, Channel ch);
}
