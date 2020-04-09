package com.lzs.chat.base.constans;


import io.netty.util.AttributeKey;

public class AppConstants {
    public static final AttributeKey<String> KEY_CONN_ID = AttributeKey.valueOf("conn_key");
    public static final AttributeKey<String> KEY_ROOM_ID = AttributeKey.valueOf("room_key");
    //返回码 1 成功 0失败
    public static final int SUCCESS_CODE=1;
    public static final int FAIL_CODE=0;

    //认证
    public static final int OP_AUTH = 1;
    public static final int OP_AUTH_REPLY = 2;
    //心跳
    public static final int OP_HEARTBEAT = 3;
    public static final int OP_HEARTBEAT_REPLY = 4;
    //消息
    public static final int OP_MESSAGE = 5;
    public static final int OP_MESSAGE_REPLY = 6;

}
