package com.lzs.chat.server.service.impl;

import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.base.constans.CmdConstants;
import com.lzs.chat.base.protobuf.Message;
import com.lzs.chat.base.service.MsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 消息服务
 * <p>
 */
@Service("msgService")
@Slf4j
public class MsgServiceImpl implements MsgService {
    @Override
    public Message.Response receive(Message.Request request) {
        Message.Response.Builder builder = Message.Response.newBuilder();
        builder.setOperation(AppConstants.OP_MESSAGE_REPLY);
        builder.setCode(AppConstants.SUCCESS_CODE);
        switch (request.getOperation()) {
            case 5:
                log.info("发送消息");
                if(request.getCmd().equals(CmdConstants.USER_SEND_MSG_CMD)){
                    //发送消息给其他用户
                }
                break;
            case 6:
                log.info("回复消息");
                break;
        }
//        Message.MsgData data;
//        try {
//            data = Message.MsgData.parseFrom(proto.getBody());
//        } catch (InvalidProtocolBufferException e) {
//            logger.error("invalid proto {} {}", proto, e.getMessage());
//        }
        // TODO
        return builder.build();
    }

}
