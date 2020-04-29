package com.lzs.chat.base.dto.resp;

import com.lzs.chat.base.bean.MonitorInfoBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/9 19:07
 * @since <版本号>
 */
@ApiModel(value = "连接信息")
@Data
@Builder
public class ChatInfoRespDto {
    @ApiModelProperty(value = "总在线人数")
    private int countConn;
    @ApiModelProperty(value = "总房间数")
    private int countRoom;
    @ApiModelProperty(value = "房间在线人数")
    private Map<Integer,Integer> roomOlineUsers;
    @ApiModelProperty(value = "服务信息")
    private MonitorInfoBean monitorInfoBean;
}
