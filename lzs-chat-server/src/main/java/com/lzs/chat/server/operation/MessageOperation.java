package com.lzs.chat.server.operation;

import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.base.constans.CmdConstants;
import com.lzs.chat.base.protobuf.Message;
import com.lzs.chat.base.service.MsgService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 消息操作
 */
@Component
@Slf4j
public class MessageOperation extends AbstractOperation {

    private final Logger logger = LoggerFactory.getLogger(MessageOperation.class);

    @Autowired
    private MsgService msgService;

    @Override
    public Integer op() {
        return AppConstants.OP_MESSAGE;
    }

    @Override
    public void action(Channel ch, Message.Protocol protocol) throws Exception {
        //首先验证
        //TODO
        //checkAuth(ch);
        //处理消息
        String cmd = protocol.getCmd();
        switch (protocol.getOperation()) {
            case 5:
                log.info("收到客户端发送来的消息");
                    //发送消息给其他用户
                    Class clz = msgService.getClass();
                    String methodName= CmdConstants.CMD_METHOD_MAP.get(cmd);
                    Method method = clz.getMethod(methodName, Message.Protocol.class, Channel.class);
                    if(Objects.nonNull(method)){
                        method.invoke(msgService,protocol,ch);
                    }
                break;
            case 6:
                log.info("收到客户端回复来的消息");
                break;
        }

        // write message reply
    }

}
