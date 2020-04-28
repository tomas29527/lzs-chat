package com.lzs.chat.base.exception;

/**
 * 认证异常类
 */
public class NotAuthException extends ChatException {
    public NotAuthException() {
        super("未登录认证");
    }
}
