package com.lzs.chat.server.handler;

import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.base.enums.AppEnum;
import com.lzs.chat.base.protobuf.Message;
import com.lzs.chat.server.ChatOperation;
import com.lzs.chat.server.connManager.ConnManagerUtil;
import com.lzs.chat.server.operation.Operation;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * 消息处理类
 */
@Service
@Scope("prototype")
@Slf4j
public class ChatServerHandler extends SimpleChannelInboundHandler<Message.Protocol> {

    @Autowired
    private ChatOperation chatOperation;



    /**
     * 客户端关闭
     *
     * @param ctx 上下文
     * @throws Exception 异常
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端与服务端连接关闭");
        ConnManagerUtil.closeConn(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message.Protocol protocol) throws Exception {
        log.info("收到消息 KEY_CONN_ID: {}", ctx.channel().attr(AppConstants.KEY_CONN_ID).get());
        Operation op = chatOperation.find(protocol.getOperation());
        if (op != null) {
            op.action(ctx.channel(), protocol);
        } else {
            log.warn("Not found operationId: " + protocol.getOperation());
        }
    }

    /**
     * 异常消息
     *
     * @param ctx   通道上下文
     * @param cause 线程
     * @throws Exception 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("异常消息", cause);
        Message.Protocol response = Message.Protocol.newBuilder()
                .setCode(AppEnum.SYSTEM_ERROR.getCode())
                .setOperation(AppConstants.OP_MESSAGE_REPLY).build();

        ctx.writeAndFlush(response);
        //ctx.close();
    }

}
