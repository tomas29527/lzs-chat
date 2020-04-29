package com.lzs.chat.base.dto.req;

import lombok.Data;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *              用户加入房间dto
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/28 15:39
 * @since <版本号>
 */
@Data
public class UserJoinRoomReqDto {
    private String userId;
    private Integer roomId;
}
