package com.lzs.chat.base.dto.req;

import lombok.Data;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/11 10:58
 * @since <版本号>
 */
@Data
public class SendMsgReqDto {
    private String userId;
    private Integer roomId;
    private String msg;
}
