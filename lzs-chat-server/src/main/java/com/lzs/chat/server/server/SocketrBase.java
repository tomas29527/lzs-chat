package com.lzs.chat.server.server;

import com.lzs.chat.base.enums.SocketType;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/29 12:06
 * @since <版本号>
 */
@Slf4j
public class SocketrBase {

    protected ThreadFactory bossThreadFactory(String name) {
        return new DefaultThreadFactory(name, Thread.MAX_PRIORITY);
    }

    protected ThreadFactory workerThreadFactory(String name) {
        return new DefaultThreadFactory(name, Thread.MAX_PRIORITY);
    }

    protected EventLoopGroup initEventLoopGroup(int nThreads, ThreadFactory tFactory) {
        SocketType socketType = socketType();
        log.info("======启动服务选择的:socketType :{}",socketType);
        switch (socketType) {
            case NATIVE_EPOLL:
                return new EpollEventLoopGroup(nThreads, tFactory);
            case NATIVE_KQUEUE:
                return new KQueueEventLoopGroup(nThreads, tFactory);
            case JAVA_NIO:
                return new NioEventLoopGroup(nThreads, tFactory);
            default:
                throw new IllegalStateException("Invalid socket type: " + socketType);
        }
    }

    protected SocketType socketType() {
        if ( NativeSupport.isNativeEPollAvailable()) {
            // netty provides the native socket transport for Linux using JNI.
            return SocketType.NATIVE_EPOLL;
        }
        if ( NativeSupport.isNativeKQueueAvailable()) {
            // netty provides the native socket transport for BSD systems such as MacOS using JNI.
            return SocketType.NATIVE_KQUEUE;
        }
        return SocketType.JAVA_NIO;
    }
}
