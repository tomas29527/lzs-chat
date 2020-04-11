package com.lzs.chat.server.operation;

import com.alibaba.fastjson.JSON;
import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.base.constans.CmdConstants;
import com.lzs.chat.base.dto.req.AuthReqDto;
import com.lzs.chat.base.protobuf.Message;
import com.lzs.chat.base.service.AuthService;
import com.lzs.chat.base.util.ProtocolUtil;
import com.lzs.chat.server.connManager.ConnManagerUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 认证操作
 */
@Component
@Slf4j
public class AuthOperation extends AbstractOperation {

    @Autowired
    private AuthService authService;

    @Override
    public Integer op() {
        return AppConstants.OP_AUTH;
    }

    @Override
    public void action(Channel ch, Message.Protocol protocol) throws Exception {
        AuthReqDto authReq = JSON.parseObject(protocol.getData(), AuthReqDto.class);
        // connection auth
        Message.Protocol response = authService.auth(protocol,authReq);
        if(AppConstants.SUCCESS_CODE.equals(response.getCode())){
            //再把根据房间id 分别放入
            //把连接id放到缓存的房间里面
            if(Objects.nonNull(authReq.getRoomId())){
                if(!ch.hasAttr(AppConstants.KEY_ROOM_ID)){
                    ch.attr(AppConstants.KEY_ROOM_ID).set(String.valueOf(authReq.getRoomId()));
                }
                ConnManagerUtil.roomConnPut(authReq.getRoomId(),getKey(ch));

                //新用户加入直播间 通知大家
                Message.Protocol onlienProto = ProtocolUtil.buildUserOnlienMsg(AppConstants.OP_MESSAGE, CmdConstants.USER_ONLINE_CMD, authReq.getUserId());
                ConnManagerUtil.sendMsgToRoomOtherConn(authReq.getRoomId(),
                        ch.attr(AppConstants.KEY_CONN_ID).get(),onlienProto);
            }
        }
        // write reply
        ch.writeAndFlush(response);
        log.debug("auth ok");
    }

}
