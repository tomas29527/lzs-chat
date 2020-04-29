package com.lzs.chat.service.service.impl;

import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.base.dto.req.AuthReqDto;
import com.lzs.chat.base.enums.AppEnum;
import com.lzs.chat.base.protobuf.Message;
import com.lzs.chat.base.util.JWTUtil;
import com.lzs.chat.base.util.ProtocolUtil;
import com.lzs.chat.service.entity.User;
import com.lzs.chat.service.service.AuthService;
import com.lzs.chat.service.service.UserService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 受权
 * <p>
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Override
    public void auth(Message.Protocol protocol, AuthReqDto authReq, Channel ch) {
        Message.Protocol response = null;
        User userInfo = null;
        //验证用户id,生成token
        String userId = authReq.getUserId();
        if (StringUtils.isNotBlank(userId)) {
            //查找用户
            userInfo = userService.getUserBiUserId(userId);
            if (Objects.nonNull(userInfo)) {
                String token = JWTUtil.createToken(userId);
                response = ProtocolUtil.buildAuthSucessResponse(AppConstants.OP_AUTH_REPLY, AppConstants.SUCCESS_CODE, token);
            } else {
                response = ProtocolUtil.buildAuthFailResponse(AppConstants.OP_AUTH_REPLY, AppEnum.SYSTEM_USERID_ERROR.getCode());
            }
        } else {
            //没有userId，认为是游客,暂时不加游客的访问,直接返回null
            response = ProtocolUtil.buildAuthFailResponse(AppConstants.OP_AUTH_REPLY, AppEnum.SYSTEM_USERID_ERROR.getCode());
        }

        if (Objects.nonNull(response) && AppConstants.SUCCESS_CODE == response.getCode()) {
            ch.writeAndFlush(response);
            log.info("认证成功 response：{}", response);
        } else {
            ChannelFuture channelFuture = ch.writeAndFlush(response);
            if (channelFuture.isDone()) {
                log.warn("认证失败关闭连接 ch :{} response:{}", ch, response);
                ch.close();
            }
        }

    }


}
