package com.lzs.chat.server.operation;

import com.lzs.chat.base.protobuf.Message;
import io.netty.channel.Channel;

/**
 * 操作类
 */
public interface Operation {

    Integer op();

    void action(Channel ch, Message.Protocol protocol) throws Exception;

}
