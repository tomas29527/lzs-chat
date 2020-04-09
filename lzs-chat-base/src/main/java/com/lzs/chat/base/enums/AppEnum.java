package com.lzs.chat.base.enums;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/9 21:19
 * @since <版本号>
 */
public enum AppEnum {
    /**
     * 错误码从 01 开始 不要乱定义
     */
    SYSTEM_ERROR(01, "系统异常"),;

    private int code;
    private String msg;

    AppEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
