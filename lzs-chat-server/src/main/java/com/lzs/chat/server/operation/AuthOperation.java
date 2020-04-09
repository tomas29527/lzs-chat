package com.lzs.chat.server.operation;

import com.alibaba.fastjson.JSON;
import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.base.dto.req.AuthReqDto;
import com.lzs.chat.base.protobuf.Message;
import com.lzs.chat.base.service.AuthService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public void action(Channel ch, Message.Request request) throws Exception {

        AuthReqDto authReq = JSON.parseObject(request.getData(), AuthReqDto.class);
        // connection auth
        Message.Response response = authService.auth(request,authReq);
        if(AppConstants.SUCCESS_CODE==response.getCode()){
           // setKey(ch, authReq.getUserId());

            //把连接信息 放入缓存
//            Client client=  Client.builder()
//                    .userId(authReq.getUserId())
//                    .roomId(authReq.getRoomId())
//                    .channel(ch)
//                    .createTime(System.currentTimeMillis()).build();
            //SystemCache.CLIENT_MAP.put(authReq.getUserId(),client);
            //再把根据房间id 分别放入
        }
        // write reply
        ch.writeAndFlush(response);
        log.debug("auth ok");
    }

}
