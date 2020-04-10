package com.lzs.chat.server.operation;

import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.base.protobuf.Message;
import com.lzs.chat.base.service.MsgService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息操作
 */
@Component
public class MessageOperation extends AbstractOperation {

    private final Logger logger = LoggerFactory.getLogger(MessageOperation.class);

    @Autowired
    private MsgService msgService;

    @Override
    public Integer op() {
        return AppConstants.OP_MESSAGE;
    }

    @Override
    public void action(Channel ch, Message.Request request) throws Exception {
        //首先验证
        //TODO
        //checkAuth(ch);
        //处理消息
        //TODO
        //receive a message
        int operation = request.getOperation();
        String cmd = request.getCmd();
        String data = request.getData();

        Message.Response resp = msgService.receive(request);

        // write message reply
        ch.writeAndFlush(resp);
    }

}
