package com.lzs.chat.service.service.impl;

import com.lzs.chat.service.entity.RoomInfo;
import com.lzs.chat.service.mapper.RoomInfoMapper;
import com.lzs.chat.service.service.RoomInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/27 10:32
 * @since <版本号>
 */
@Service
public class RoomInfoServiceImpl implements RoomInfoService {
    @Autowired
    private RoomInfoMapper roomInfoMapper;

    @Override
    public RoomInfo getRoomInfoById(Integer roomId) {
        return roomInfoMapper.selectById(roomId);
    }
}
