package com.lzs.chat.base.dto.req;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Digits;
import java.io.Serializable;

/**
 * @date 2018/4/16
 */
public class BaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请求时间 从1970年到现在的毫秒
     */
    @Digits(integer = 10, fraction = 0, message = "ctime参数不合法")
    @ApiModelProperty(value = "请求时间 从1970年到现在的毫秒", name = "ctime", required = true)
    private Long ctime;

    /**
     * 请求来源ip 客户端不用传由后台自动获取
     */
    @ApiModelProperty(value = "请求来源ip 客户端不用传由后台自动获取", name = "cip")
    private String cip;

    /**
     * 用户id
     */
    @Digits(integer = 20, fraction = 0, message = "uid参数不合法")
    @ApiModelProperty(value = "用户id", name = "uid")
    private Long uid;

    /**
     * 令牌
     */
    @ApiModelProperty(value = "令牌", name = "token")
    private String token;

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public String getCip() {
        return cip;
    }

    public void setCip(String cip) {
        this.cip = cip;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public String toString() {
        return "BaseRequest{" +
                "ctime=" + ctime +
                ", cip='" + cip + '\'' +
                ", uid=" + uid +
                ", token='" + token + '\'' +
                '}';
    }
}
