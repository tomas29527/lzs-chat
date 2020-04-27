package com.lzs.chat.base.dto.resp;

import lombok.Builder;
import lombok.Data;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *              认证成功返回dto
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/27 19:10
 * @since <版本号>
 */
@Data
@Builder
public class AuthRespDto {
    private String userId;
    private String nickName;
}
