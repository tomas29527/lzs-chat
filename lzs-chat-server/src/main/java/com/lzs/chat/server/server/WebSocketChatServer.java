package com.lzs.chat.server.server;

import com.lzs.chat.server.initializer.WebSocketServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * websocket server
 * <p>
 * Created by Tony on 4/13/16.
 */
@Component("webSocketChatServer")
@Slf4j
public class WebSocketChatServer implements ChatServer {

    @Value("${chat.websocket.port:9090}")
    private int port;
    @Autowired
    private WebSocketServerInitializer serverInitializer;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workGroup = new NioEventLoopGroup();

    private ChannelFuture channelFuture;

    @Override
    public void start() throws Exception {
        try {
            ServerBootstrap b = new ServerBootstrap()
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(serverInitializer)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            //写入把服务信息写入到 zookeeper中
            // TODO: 2020/4/12  
            log.info("Starting WebSocketChatServer... Port: " + port);
            channelFuture = b.bind(port).sync();
        } finally {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    shutdown();
                }
            });
        }
    }

    @Override
    public void restart() throws Exception {
        shutdown();
        start();
    }

    @Override
    public void shutdown() {
        if (channelFuture != null) {
            channelFuture.channel().close().syncUninterruptibly();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workGroup != null) {
            workGroup.shutdownGracefully();
        }
    }

}
