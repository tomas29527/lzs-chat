package com.lzs.chat.server.service.impl;

import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.base.dto.req.AuthReqDto;
import com.lzs.chat.base.enums.AppEnum;
import com.lzs.chat.base.protobuf.Message;
import com.lzs.chat.base.service.AuthService;
import com.lzs.chat.base.util.ProtocolUtil;
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
    public Message.Protocol auth(Message.Protocol protocol,AuthReqDto authReq)  {

        //cacheManager.getCache(CachingConfig.ONLINE).put(req.getUid(), appkey);
        //验证appkey
        //TODO
        //验证用户是否存在,房间号是否存在
        //TODO
        //生成token
        //TODO
        //把链接信息放入本地缓存和redis
        //TODO
        log.debug("auth appkey={}", protocol.getAppkey());

        if(!appkey.equals(protocol.getAppkey())){
            Message.Protocol resp = ProtocolUtil.buildAuthFailResponse(AppConstants.OP_AUTH_REPLY, AppEnum.SYSTEM_ERROR_APPKEY.getCode());
            return resp;
        }
        //检查是否有房间号
        if(Objects.nonNull(authReq.getRoomId())){
            //首先检查房间号是否存在，是否开播
            //todo

        }
        //验证用户id,生成token
        return ProtocolUtil.buildAuthSucessResponse(AppConstants.OP_AUTH_REPLY,AppConstants.SUCCESS_CODE,"abcd1234");
    }


}
