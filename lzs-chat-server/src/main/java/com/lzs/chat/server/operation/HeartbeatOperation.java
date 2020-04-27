package com.lzs.chat.server.operation;

import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.base.protobuf.Message;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 心跳
 */
@Component
public class HeartbeatOperation  extends AbstractOperation {
    @Value("${chat.appkey}")
    private String appkey;
    @Override
    public Integer op() {
        return AppConstants.OP_HEARTBEAT;
    }

    @Override
    public void action(Channel ch, Message.Protocol protocol) throws Exception {
        //验证appkey
        authAppKey(protocol,appkey);
        // 验证token
        authToken(protocol);
    }
}
