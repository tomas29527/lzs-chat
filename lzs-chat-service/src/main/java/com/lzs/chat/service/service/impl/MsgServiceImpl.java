package com.lzs.chat.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.lzs.chat.base.connManager.ConnManagerUtil;
import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.base.constans.CmdConstants;
import com.lzs.chat.base.dto.req.SendMsgReqDto;
import com.lzs.chat.base.dto.req.UserJoinRoomReqDto;
import com.lzs.chat.base.dto.resp.AuthRespDto;
import com.lzs.chat.base.enums.AppEnum;
import com.lzs.chat.base.protobuf.Message;
import com.lzs.chat.base.util.ProtocolUtil;
import com.lzs.chat.service.entity.RoomInfo;
import com.lzs.chat.service.entity.User;
import com.lzs.chat.service.service.MsgService;
import com.lzs.chat.service.service.RoomInfoService;
import com.lzs.chat.service.service.UserService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * 消息服务
 * <p>
 */
@Service("msgService")
@Slf4j
public class MsgServiceImpl implements MsgService {
    @Autowired
    private RoomInfoService roomInfoService;
    @Autowired
    private UserService userService;

    /**
     * 房间号，用户id检查
     *
     * @param roomId
     * @param userInfo
     * @param ch
     */
    private boolean checkParam(Integer roomId, User userInfo, Channel ch) {
        Message.Protocol response = ProtocolUtil.buildAuthFailResponse(AppConstants.OP_MESSAGE_REPLY, AppConstants.SUCCESS_CODE);
        ;
        //检查房间id是否存在
        RoomInfo roomInfoById = roomInfoService.getRoomInfoById(roomId);
        if (Objects.isNull(roomInfoById)) {
            response = ProtocolUtil.buildAuthFailResponse(AppConstants.OP_MESSAGE_REPLY, AppEnum.SYSTEM_ROOMID_ERROR.getCode());
        } else {
            //检查用户是否存在
            if (Objects.isNull(userInfo)) {
                response = ProtocolUtil.buildAuthFailResponse(AppConstants.OP_MESSAGE_REPLY, AppEnum.SYSTEM_ROOMID_ERROR.getCode());
            }
        }
        //验证通过
        if (AppConstants.SUCCESS_CODE != response.getCode()) {
            ch.writeAndFlush(response);
            return false;
        }

        return true;
    }

    /**
     * 检查用户是否加入房间
     *
     * @param connId
     * @param roomId
     * @return
     */
    private boolean cheackUserInRoom(String connId, Integer roomId) {
        List<String> connIds = ConnManagerUtil.connsInRoom(roomId);
        if (!CollectionUtils.isEmpty(connIds)) {
            return connIds.contains(connId);
        }
        return false;
    }

    @Override
    public void sendMsgToOther(Message.Protocol protocol, Channel ch) {
        SendMsgReqDto sendMsgReqDto = JSON.parseObject(protocol.getData(), SendMsgReqDto.class);
        Integer roomId = sendMsgReqDto.getRoomId();
        String userId = sendMsgReqDto.getUserId();
        User userInfo = userService.getUserBiUserId(userId);
        if (checkParam(roomId, userInfo, ch)) {
            String connId = ch.attr(AppConstants.KEY_CONN_ID).get();
            //检查用户是否在房间内，不在房间不允许发言
            if (cheackUserInRoom(connId, roomId)) {
                //发送的消息
                Message.Protocol protoMsg = ProtocolUtil.buildUserSendMsg(AppConstants.OP_MESSAGE,
                        CmdConstants.USER_SEND_MSG_CMD, sendMsgReqDto.getMsg());

                ConnManagerUtil.sendMsgToRoomOtherConn(sendMsgReqDto.getRoomId(), connId, protoMsg);
            } else {
                //回复不能发言
                Message.Protocol resp = ProtocolUtil.buildResponse(AppConstants.OP_MESSAGE_REPLY, AppEnum.MESSAGE_USER_SEND_ERROR.getCode(), CmdConstants.USER_JOIN_ROOM_CMD);
                ch.writeAndFlush(resp);
            }
        }
    }

    @Override
    public void userJoinRoom(Message.Protocol protocol, Channel ch) {
        UserJoinRoomReqDto userJoinRoomReqDto = JSON.parseObject(protocol.getData(), UserJoinRoomReqDto.class);
        Integer roomId = userJoinRoomReqDto.getRoomId();
        String userId = userJoinRoomReqDto.getUserId();
        String connId = ch.attr(AppConstants.KEY_CONN_ID).get();
        User userInfo = userService.getUserBiUserId(userId);
        if (checkParam(roomId, userInfo, ch)) {
            //先给连接绑定一个房间id的属性,先检查是否已经有绑定过属性
            if (ch.hasAttr(AppConstants.KEY_ROOM_ID)) {
                //已绑定说明，加入过房间,先删除以前房间的连接id
                Integer oldRoomId = ch.attr(AppConstants.KEY_ROOM_ID).get();
                ConnManagerUtil.roomConnRemove(oldRoomId, connId);
                //再加入新的房间
                ConnManagerUtil.roomConnPut(roomId, connId);
            } else {
                ch.attr(AppConstants.KEY_ROOM_ID).set(roomId);
                //加入房间
                ConnManagerUtil.roomConnPut(roomId, connId);
            }
            //回复加入房间成功
            Message.Protocol joinResp = ProtocolUtil.buildResponse(AppConstants.OP_MESSAGE_REPLY, AppConstants.SUCCESS_CODE, CmdConstants.USER_JOIN_ROOM_CMD);
            ch.writeAndFlush(joinResp);
            //通知新用户上线
            AuthRespDto dto = AuthRespDto.builder()
                    .userId(userId)
                    .nickName(userInfo.getNickName()).build();
            String data = JSON.toJSONString(dto);
            Message.Protocol onlienProto = ProtocolUtil.buildUserOnlienMsg(AppConstants.OP_MESSAGE, CmdConstants.USER_ONLINE_CMD, data);
            ConnManagerUtil.sendMsgToRoomOtherConn(roomId,
                    connId, onlienProto);
        }
    }


}
