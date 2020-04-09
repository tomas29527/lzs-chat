package com.lzs.chat.server.connManager;

import com.google.common.collect.Maps;
import com.lzs.chat.base.bean.Client;
import com.lzs.chat.base.protobuf.Message;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/9 15:58
 * @since <版本号>
 */
@Slf4j
public class ConnManagerUtil {
    /**
     * 所有连接对象
     */
    private static Map<String, Client> CLIENT_MAP = Maps.newConcurrentMap();
    /**
     * 按照房间id存放 用户id
     */
    private static Map<Integer, List<String>> ROOM_CONN_MAP = Maps.newConcurrentMap();

    /**
     * 连接操作
     * @param connId
     * @param client
     */
    public static void clientPut(String connId, Client client) {
        CLIENT_MAP.put(connId, client);
    }

    public static void clientGet(String connId) {
        CLIENT_MAP.get(connId);
    }

    public static void clientRemove(String connId) {
        CLIENT_MAP.remove(connId);
    }

    public static int countClient() {
        return CLIENT_MAP.size();
    }

    /**
     * 房间操作
     * @param roomId
     * @param connId
     */
    public static void roomConnPut(Integer roomId, String connId) {
        List<String> connIds = ROOM_CONN_MAP.get(roomId);
        //需要枷锁
        synchronized (ROOM_CONN_MAP) {
            if (CollectionUtils.isEmpty(connIds)) {
                connIds = new ArrayList<>();
                connIds.add(connId);
                ROOM_CONN_MAP.put(roomId,connIds);
            } else {
                connIds.add(connId);
            }
        }
    }

    public static void roomConnRemove(Integer roomId, String connId){
        List<String> connIds = ROOM_CONN_MAP.get(roomId);
        long oldtime=System.currentTimeMillis();
        //需要枷锁
        synchronized (ROOM_CONN_MAP) {
            if (CollectionUtils.isEmpty(connIds)) {
                Iterator<String> iterator = connIds.iterator();
                while (iterator.hasNext()){
                    String next = iterator.next();
                    if(next.equals(connId)){
                        iterator.remove();
                    }
                }
            }
        }
        long newtime=System.currentTimeMillis();
        //当循环大于20ms打报警日志
        if(newtime-oldtime>20){
            log.warn("========删除房间人数时间有点长 roomsize:{}",connIds.size());
        }
    }

    /**
     * 总房间数
     * @return
     */
    public static int countRoom(){
       return  ROOM_CONN_MAP.size();
    }

    /**
     * 房间总人数
     * @param roomId
     * @return
     */
    public static int countRoomConn(Integer roomId){
        List<String> connIds = ROOM_CONN_MAP.get(roomId);
        return  connIds.size();
    }

    /**
     * 给房间所有用户发信息
     * @param roomId
     * @param response
     */
    public static void sendMsgToRoomConn(Integer roomId, Message.Response response){
        List<String> connIds = ROOM_CONN_MAP.get(roomId);
        long oldtime=System.currentTimeMillis();
        for (String connId : connIds) {
            Client client = CLIENT_MAP.get(connId);
            if(Objects.isNull(client)){
                Channel channel = client.getChannel();
                if(channel.isActive()){
                    channel.writeAndFlush(response);
                }
            }
        }
        long newtime=System.currentTimeMillis();
        //当循环大于50ms打报警日志
        if(newtime-oldtime>50){
            log.warn("========房间人数发消息时间有点长 roomsize:{}",connIds.size());
        }
    }
}
