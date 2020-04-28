package com.lzs.chat.base.exception;

/**
 * 认证异常类
 */
public class AuthFailException extends ChatException {
    public AuthFailException() {
        super("认证失败");
    }
}
