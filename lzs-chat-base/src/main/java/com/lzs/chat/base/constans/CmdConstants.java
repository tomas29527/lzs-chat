package com.lzs.chat.base.constans;

import java.util.HashMap;
import java.util.Map;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *            操作实例类
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/10 20:11
 * @since <版本号>
 */
public class CmdConstants {
    /**
     * 命令个方法的对应map
     */
    public static Map<String,String> CMD_METHOD_MAP=new HashMap<>();

    static {
        /**
         * 发送消息给其他用户
         */
        CMD_METHOD_MAP.put("user_send_msg_cmd","sendMsgToOther");
    }

    /**
     * 用户上线cmd
     */
    public static  String USER_ONLINE_CMD ="user_online_cmd";
    /**
     * 用户发消息cmd
     */
    public static  String USER_SEND_MSG_CMD ="user_send_msg_cmd";
}
