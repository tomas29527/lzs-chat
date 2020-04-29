package com.lzs.chat.base.enums;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/29 11:58
 * @since <版本号>
 */
public enum SocketType {
    JAVA_NIO,
    NATIVE_EPOLL,           // for linux
    NATIVE_KQUEUE,          // for bsd systems
    NATIVE_EPOLL_DOMAIN,    // unix domain socket for linux
    NATIVE_KQUEUE_DOMAIN    // unix domain socket for bsd systems
}
