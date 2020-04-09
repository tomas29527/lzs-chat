package com.lzs.chat.base.dto.req;

import lombok.Data;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *    认证请求dto
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/9 16:33
 * @since <版本号>
 */
@Data
public class AuthReqDto {
  private String userId;
  private Integer roomId;
}
