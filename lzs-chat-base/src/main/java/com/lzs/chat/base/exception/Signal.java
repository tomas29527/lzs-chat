package com.lzs.chat.base.exception;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/25 21:45
 * @since <版本号>
 */
public class Signal  extends Exception {
    public Signal(String message) {
        super(message);
    }

    public Signal(String message, Throwable cause) {
        super(message, cause);
    }
}
