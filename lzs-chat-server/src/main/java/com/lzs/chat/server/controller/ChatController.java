package com.lzs.chat.server.controller;

import com.lzs.chat.base.bean.MonitorInfoBean;
import com.lzs.chat.base.dto.resp.ChatInfoRespDto;
import com.lzs.chat.base.connManager.ConnManagerUtil;
import com.lzs.chat.server.util.MonitorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/7 16:26
 * @since <版本号>
 */
@Controller
@Slf4j
public class ChatController {

    @GetMapping(value = {"/index", ""})
    public String index(Model model, String userId, Integer roomId) {
        log.info("===========================");
        model.addAttribute("userId",userId);
        model.addAttribute("roomId",roomId);
        return "demo";
    }

    @GetMapping("/chatInfo")
    @ResponseBody
    public ChatInfoRespDto chatInfo(){
        Map<Integer,Integer> roomOlineUsers =new HashMap<>();
        Set<Integer> integers = ConnManagerUtil.roomIdList();

        for (Integer integer : integers) {
            roomOlineUsers.put(integer,ConnManagerUtil.countRoomConn(integer));
        }
        MonitorInfoBean monitorInfoBean = MonitorUtil.getMonitorInfoBean();
        return  ChatInfoRespDto.builder()
                .countConn(ConnManagerUtil.countClient())
                 .countRoom(ConnManagerUtil.countRoom())
                .roomOlineUsers(roomOlineUsers)
                .monitorInfoBean(monitorInfoBean)
                .build();
    }


}
