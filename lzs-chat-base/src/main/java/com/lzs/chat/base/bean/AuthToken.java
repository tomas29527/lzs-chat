package com.lzs.chat.base.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 用户验证类
 */
@Data
public class AuthToken {
    @JsonProperty("user_id")
    private long userId;
    @JsonProperty("token")
    private String token;
}
