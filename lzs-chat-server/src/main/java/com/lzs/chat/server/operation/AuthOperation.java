package com.lzs.chat.server.operation;

import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.base.dto.req.AuthReqDto;
import com.lzs.chat.base.protobuf.Message;
import com.lzs.chat.base.util.ThreadUtil;
import com.lzs.chat.server.service.AuthService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 认证操作
 */
@Component
@Slf4j
public class AuthOperation extends AbstractOperation {
    @Value("${chat.appkey}")
    private String appkey;
    @Autowired
    private AuthService authService;

    @Override
    public Integer op() {
        return AppConstants.OP_AUTH;
    }

    @Override
    public void action(Channel ch, Message.Protocol protocol) throws Exception {
        //验证appkey
        authAppKey(protocol,appkey);
        //认证的参数验证
        AuthReqDto authReq = authParamsCheck(protocol);
        //验证key
        log.debug("auth appkey={}", protocol.getAppkey());
        ThreadUtil.submit(() -> {
            // connection auth
            try {
                authService.auth(protocol, authReq,ch);
            }catch (Exception e){
                log.error("验证处理异常 error:",e);
            }
        });

    }

}
