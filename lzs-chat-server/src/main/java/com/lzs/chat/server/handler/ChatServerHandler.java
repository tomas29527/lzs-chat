package com.lzs.chat.server.handler;

import com.lzs.chat.base.bean.Client;
import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.base.enums.AppEnum;
import com.lzs.chat.base.protobuf.Message;
import com.lzs.chat.base.util.SnowFlake;
import com.lzs.chat.server.ChatOperation;
import com.lzs.chat.base.connManager.ConnManagerUtil;
import com.lzs.chat.base.exception.ChatException;
import com.lzs.chat.base.exception.Signal;
import com.lzs.chat.server.operation.Operation;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 消息处理类
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class ChatServerHandler extends SimpleChannelInboundHandler<Message.Protocol> {
    @Autowired
    private ChatOperation chatOperation;

    /**
     * 客户端连接
     *
     * @param ctx 上下文
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel ch = ctx.channel();
        // 添加连接信息
        String uniqueCode = SnowFlake.getUniqueCode();
        //设置连接id
        ch.attr(AppConstants.KEY_CONN_ID).set(uniqueCode);
        log.info("==========客户端与服务端连接开启========== chId:{}",uniqueCode);
        //加入到连接缓存
        Client client = Client.builder()
                .channel(ch)
                .createTime(System.currentTimeMillis())
                .ip(ch.remoteAddress().toString())
                .build();
        ConnManagerUtil.clientPut(uniqueCode, client);
    }

    /**
     * 客户端关闭
     *
     * @param ctx 上下文
     * @throws Exception 异常
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端connid: {} 与服务端连接关闭",ctx.channel().attr(AppConstants.KEY_CONN_ID).get());
        ConnManagerUtil.closeConn(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message.Protocol protocol) throws Exception {
        log.info("收到消息 KEY_CONN_ID: {}  protocol:{}", ctx.channel().attr(AppConstants.KEY_CONN_ID).get(),protocol);
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
        Channel ch = ctx.channel();
        if(cause instanceof Signal){
            log.error("force to close channel: {} An Signal exception was caught:",ch, cause);
            ch.close();
        }else if(cause instanceof ChatException){
            log.error("force to close channel: {} An ChatException exception was caught:",ch, cause);
            ch.close();
        }
        else  if (cause instanceof IOException) {
            log.error("force to close channel: {} An I/O exception was caught:",ch, cause);
            ch.close();
        } else if (cause instanceof DecoderException) {
            log.error("force to close channel: {} Decoder exception was caught: ", ch,cause);
            ch.close();
        }else {
            log.error("channel: {} Unexpected exception was caught:",ch, cause);
            Message.Protocol response = Message.Protocol.newBuilder()
                    .setCode(AppEnum.SYSTEM_ERROR.getCode())
                    .setOperation(AppConstants.OP_MESSAGE_REPLY).build();
            ch.writeAndFlush(response);
        }

        //ctx.close();
    }

}
