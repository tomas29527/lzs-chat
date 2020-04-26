package com.lzs.chat.server.handler;

import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.server.exception.Signal;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/13 16:03
 * @since <版本号>
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class ChatHeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent)evt;
            log.info("心跳检查:conn_id {}", ctx.channel().attr(AppConstants.KEY_CONN_ID).get());
            switch (event.state()){
                case READER_IDLE:
                    throw new Signal("很久未收到消息");
            }
        }else {
            super.userEventTriggered(ctx, evt);
        }

    }

}
