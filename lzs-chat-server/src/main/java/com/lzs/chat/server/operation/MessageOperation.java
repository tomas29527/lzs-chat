package com.lzs.chat.server.operation;

import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.base.constans.CmdConstants;
import com.lzs.chat.base.protobuf.Message;
import com.lzs.chat.base.util.Reflects;
import com.lzs.chat.server.service.MsgService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 消息操作
 */
@Component
@Slf4j
public class MessageOperation extends AbstractOperation {
    @Value("${chat.appkey}")
    private String appkey;
    @Autowired
    private MsgService msgService;

    @Override
    public Integer op() {
        return AppConstants.OP_MESSAGE;
    }

    @Override
    public void action(Channel ch, Message.Protocol protocol) throws Exception {
        //验证appkey
        authAppKey(protocol,appkey);
        // 验证token
        authToken(protocol);
        //处理消息
        String cmd = protocol.getCmd();
        switch (protocol.getOperation()) {
            case 5:
                log.info("收到客户端发送来的消息");
                //发送消息给其他用户
                Class clz = msgService.getClass();
                String methodName = CmdConstants.CMD_METHOD_MAP.get(cmd);
                if(StringUtils.isNotBlank(methodName)){
                    Class<?>[] parameterTypes = new Class<?>[]{Message.Protocol.class, Channel.class};
                    Object[] args = new Object[]{protocol, ch};
                    Reflects.fastInvoke(msgService, methodName, parameterTypes, args);
                }else {
                    log.error("========cmd 参数错误========");
                    //关闭连接
                    ch.close();
                }
                break;
            case 6:
                log.info("收到客户端回复来的消息");
                break;
        }

        // write message reply
    }

}
