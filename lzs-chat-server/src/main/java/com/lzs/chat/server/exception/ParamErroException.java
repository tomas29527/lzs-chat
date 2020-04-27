package com.lzs.chat.server.exception;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *   参数错误异常
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/27 13:40
 * @since <版本号>
 */
public class ParamErroException extends ChatException {
    public ParamErroException() {
        super("参数错误");
    }
}
