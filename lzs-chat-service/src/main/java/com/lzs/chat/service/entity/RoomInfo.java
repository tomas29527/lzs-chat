package com.lzs.chat.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/27 10:40
 * @since <版本号>
 */
@Data
@TableName("lzs_room_info")
public class RoomInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String roomName;
    private String userId;
    private Integer status;
    private Integer onlineStatus;
    private Integer attentionNum;
    private Date createTime;
    private Date updateTime;
}
