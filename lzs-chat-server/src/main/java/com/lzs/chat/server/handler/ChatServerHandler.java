package com.lzs.chat.server.handler;

import com.lzs.chat.base.bean.Client;
import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.base.enums.AppEnum;
import com.lzs.chat.base.protobuf.Message;
import com.lzs.chat.base.util.SnowFlake;
import com.lzs.chat.server.ChatOperation;
import com.lzs.chat.server.connManager.ConnManagerUtil;
import com.lzs.chat.server.operation.Operation;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * 消息处理类
 */
@Sharable
@Service
@Scope("prototype")
@Slf4j
public class ChatServerHandler extends SimpleChannelInboundHandler<Message.Request> {

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
    protected void channelRead0(ChannelHandlerContext ctx, Message.Request request) throws Exception {
        log.info("收到消息 requestMsg: {}", request);
        log.info("收到消息 KEY_CONN_ID: {}", ctx.channel().attr(AppConstants.KEY_CONN_ID).get());
        Operation op = chatOperation.find(request.getOperation());
        if (op != null) {
            op.action(ctx.channel(), request);
            //msgService.receive(proto);
        } else {
            log.warn("Not found operationId: " + request.getOperation());
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
        Message.Response response = Message.Response.newBuilder()
                .setCode(AppEnum.SYSTEM_ERROR.getCode())
                .setOperation(AppConstants.OP_MESSAGE_REPLY).build();
        ctx.writeAndFlush(response);
        //ctx.close();
    }

}
