package com.lzs.chat.server.server;

import com.lzs.chat.server.initializer.WebSocketServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadFactory;

/**
 * websocket server
 * <p>
 * Created by Tony on 4/13/16.
 */
@Component("webSocketChatServer")
@Slf4j
public class WebSocketChatServer extends SocketrBase implements ChatServer {

    @Value("${chat.websocket.port:9090}")
    private int port;
    @Autowired
    private WebSocketServerInitializer serverInitializer;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    private ChannelFuture channelFuture;

    @Override
    public void start() throws Exception {
        try {
            int nWorkers= Runtime.getRuntime().availableProcessors()*2;
            ThreadFactory bossFactory = bossThreadFactory("chat.acceptor.boss");
            ThreadFactory workerFactory = workerThreadFactory("chat.acceptor.worker");
            bossGroup = initEventLoopGroup(1, bossFactory);
            workGroup = initEventLoopGroup(nWorkers, workerFactory);

            ServerBootstrap b = new ServerBootstrap()
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    //配置TCP参数，握手字符串长度设置
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //TCP_NODELAY算法，尽可能发送大块数据，减少充斥的小块数据
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childHandler(serverInitializer)
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
            bossGroup.shutdownGracefully().syncUninterruptibly();
        }
        if (workGroup != null) {
            workGroup.shutdownGracefully().syncUninterruptibly();
        }

    }

}
