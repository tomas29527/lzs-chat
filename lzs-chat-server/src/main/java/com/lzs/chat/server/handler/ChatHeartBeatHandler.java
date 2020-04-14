package com.lzs.chat.server.handler;

import com.lzs.chat.base.bean.Client;
import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.base.protobuf.Message;
import com.lzs.chat.base.util.SnowFlake;
import com.lzs.chat.server.ChatOperation;
import com.lzs.chat.server.connManager.ConnManagerUtil;
import com.lzs.chat.server.operation.Operation;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/13 16:03
 * @since <版本号>
 */
@Service
@Scope("prototype")
@Slf4j
public class ChatHeartBeatHandler extends SimpleChannelInboundHandler<Message.Protocol> {

    @Autowired
    private ChatOperation chatOperation;

    private int readIdleTimes = 0;
    /**
     * 客户端连接
     *
     * @param ctx 上下文
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端与服务端连接开启");
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

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message.Protocol protocol) throws Exception {
        log.info("收到消息 requestMsg: {}", protocol);
        if(AppConstants.OP_HEARTBEAT ==protocol.getOperation()){
            Operation op = chatOperation.find(protocol.getOperation());
            if (op != null) {
                op.action(ctx.channel(), protocol);
            } else {
                log.warn("Not found operationId: " + protocol.getOperation());
            }
        }
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {

            IdleStateEvent event = (IdleStateEvent)evt;
            log.info("心跳检查:conn_id {}", ctx.channel().attr(AppConstants.KEY_CONN_ID).get());
            String eventType = null;
            switch (event.state()){
                case READER_IDLE:
                    eventType = "读空闲";
                    readIdleTimes ++; // 读空闲的计数加1
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    // 不处理
                    break;
                case ALL_IDLE:
                    eventType ="读写空闲";
                    // 不处理
                    break;
            }
            log.info("{}超时事件：{}",ctx.channel().remoteAddress(),eventType);
            if(readIdleTimes > 3){
                log.info(" [server]读空闲超过3次，关闭连接");
                //ctx.channel().writeAndFlush("you are out");
                ConnManagerUtil.closeConn(ctx.channel());
            }
        }else {
            super.userEventTriggered(ctx, evt);
        }

    }

}
