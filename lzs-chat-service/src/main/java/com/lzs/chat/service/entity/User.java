package com.lzs.chat.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/27 11:25
 * @since <版本号>
 */
@Data
@TableName("lzs_user")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer Id;
    private String userId;
    private String parentUserId;
    private Integer levelId;
    private Integer type;
    private String nickName;
    private String password;
    private String salt;
    private String imageUrl;
    private String status;
    private String loginIp;
    private String registerIp;
}
