package com.lzs.chat.server.service.impl;

import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.base.dto.req.AuthReqDto;
import com.lzs.chat.base.protobuf.Message;
import com.lzs.chat.base.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 受权
 * <p>
 */
@Service("authService")
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Value("${chat.appkey}")
    private String appkey;
    @Override
    public Message.Response auth(Message.Request request, AuthReqDto authReq)  {

        //cacheManager.getCache(CachingConfig.ONLINE).put(req.getUid(), appkey);
        //验证appkey
        //TODO
        //验证用户是否存在,房间号是否存在
        //TODO
        //生成token
        //TODO
        //把链接信息放入本地缓存和redis
        //TODO
        log.debug("auth appkey={}", request.getAppkey());
        Message.Response.Builder respBuilder = Message.Response.newBuilder();
        respBuilder.setOperation(AppConstants.OP_AUTH_REPLY);
        if(!appkey.equals(request.getAppkey())){
            respBuilder.setCode(AppConstants.FAIL_CODE);
            respBuilder.setMsg("appkey不一致");
            return respBuilder.build();
        }
        //检查是否有房间号
        if(Objects.nonNull(authReq.getRoomId())){
            //首先检查房间号是否存在，是否开播
            //todo
            //把连接id放到缓存的房间里面

        }
        respBuilder.setCode(AppConstants.SUCCESS_CODE);
        respBuilder.setToken("abcd1234");
        return respBuilder.build();
    }


}
