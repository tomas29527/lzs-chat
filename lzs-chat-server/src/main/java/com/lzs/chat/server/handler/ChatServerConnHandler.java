package com.lzs.chat.server.handler;

import com.lzs.chat.base.bean.Client;
import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.base.util.SnowFlake;
import com.lzs.chat.server.connManager.ConnManagerUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *            连接创建handler
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/23 20:30
 * @since <版本号>
 */
@Slf4j
public class ChatServerConnHandler extends ChannelInboundHandlerAdapter {
    /**
     * 客户端连接
     * @param ctx 上下文
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("==========客户端与服务端连接开启==========");
        // 添加连接信息
        String uniqueCode = SnowFlake.getUniqueCode();
        //设置连接id
        ctx.channel().attr(AppConstants.KEY_CONN_ID).set(uniqueCode);
        //加入到连接缓存
        Client client = Client.builder()
                .channel(ctx.channel())
                .createTime(System.currentTimeMillis())
                .build();
        ConnManagerUtil.clientPut(uniqueCode, client);
    }
}
