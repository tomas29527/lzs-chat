package com.lzs.chat.base.util;

import com.lzs.chat.base.protobuf.Message;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/11 11:22
 * @since <版本号>
 */
public class ProtocolUtil {

    /**
     * 创建认证的返回信息
     * @param operation
     * @param code
     * @return
     */
    public static Message.Protocol buildAuthFailResponse(int operation,String code){
        return Message.Protocol.newBuilder()
                .setOperation(operation)
                .setCode(code).build();
    }

    /**
     * 认证成功返回信息
     * @param operation
     * @param code
     * @param token
     * @return
     */
    public static Message.Protocol buildAuthSucessResponse(int operation,String code,String token){
        return Message.Protocol.newBuilder()
                .setOperation(operation)
                .setCode(code)
                .setToken(token).build();
    }

    /**
     * 创建消息处理的回复消息
     * @param operation
     * @param code
     * @param cmd
     * @return
     */
    public static Message.Protocol buildResponse(int operation,String code,String cmd){
        return Message.Protocol.newBuilder()
                .setOperation(operation)
                .setCode(code)
                .setCmd(cmd)
                .build();
    }

    /**
     * 创建用户线上消息
     * @param operation
     * @param cmd
     * @param data
     * @return
     */
    public static Message.Protocol buildUserOnlienMsg(int operation,String cmd,String data){
        return Message.Protocol.newBuilder()
                .setOperation(operation)
                .setCmd(cmd)
                .setData(data).build();
    }

    /**
     * 创建用户发消息的消息
     * @param operation
     * @param cmd
     * @param data
     * @return
     */
    public static Message.Protocol buildUserSendMsg(int operation,String cmd,String data){
        return Message.Protocol.newBuilder()
                .setOperation(operation)
                .setCmd(cmd)
                .setData(data).build();
    }
}
