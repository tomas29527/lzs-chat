package com.lzs.chat.server.controller;

import com.lzs.chat.base.dto.resp.ChatInfoRespDto;
import com.lzs.chat.server.connManager.ConnManagerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @GetMapping(value = {"index", ""})
    public String index(Model model) {
        log.info("===========================");
        return "/demo";
    }

    @GetMapping("/chatInfo")
    @ResponseBody
    public ChatInfoRespDto chatInfo(){
        return  ChatInfoRespDto.builder()
                .countConn(ConnManagerUtil.countClient())
                 .countRoom(ConnManagerUtil.countRoom()).build();
    }
}
