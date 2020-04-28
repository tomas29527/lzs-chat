package com.lzs.chat.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.base.constans.CmdConstants;
import com.lzs.chat.base.dto.req.SendMsgReqDto;
import com.lzs.chat.base.protobuf.Message;
import com.lzs.chat.service.service.MsgService;
import com.lzs.chat.base.util.ProtocolUtil;
import com.lzs.chat.base.connManager.ConnManagerUtil;
import io.netty.channel.Channel;
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
    public Message.Protocol sendMsgToOther(Message.Protocol protocol, Channel ch) {
        SendMsgReqDto sendMsgReqDto = JSON.parseObject(protocol.getData(), SendMsgReqDto.class);
        String connId=ch.attr(AppConstants.KEY_CONN_ID).get();
        //发送的消息
        Message.Protocol protoMsg = ProtocolUtil.buildUserSendMsg(AppConstants.OP_MESSAGE,
                CmdConstants.USER_SEND_MSG_CMD, sendMsgReqDto.getMsg());

        ConnManagerUtil.sendMsgToRoomOtherConn(sendMsgReqDto.getRoomId(),connId,protoMsg);
        return null;
    }


}
