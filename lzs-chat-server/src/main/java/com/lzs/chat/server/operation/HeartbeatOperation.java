package com.lzs.chat.server.operation;

import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.base.protobuf.Message;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

/**
 * 心跳
 */
@Component
public class HeartbeatOperation  extends AbstractOperation {

    @Override
    public Integer op() {
        return AppConstants.OP_HEARTBEAT;
    }

    @Override
    public void action(Channel ch, Message.Request request) throws Exception {
        //验证消息

        // write heartbeat reply
        Message.Response.Builder respBuilder = Message.Response.newBuilder();
        respBuilder.setOperation(AppConstants.OP_AUTH_REPLY);
        respBuilder.setCode(AppConstants.SUCCESS_CODE);

        ch.writeAndFlush(respBuilder.build());
    }
}
