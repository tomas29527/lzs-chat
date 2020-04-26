package com.lzs.chat.base.bean;

import io.netty.channel.Channel;
import lombok.Builder;
import lombok.Data;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/9 14:07
 * @since <版本号>
 */
@Data
@Builder
public class Client {

    /**
     * 用户id
     */
    private String userId;
    /**
     * 房间号
     */
    private Integer roomId;

    /**
     * 连接对象
     */
    private Channel channel;
    /**
     * 连接开始时间
     */
    private long createTime;
    /**
     * ip
     */
    private String ip;
}
