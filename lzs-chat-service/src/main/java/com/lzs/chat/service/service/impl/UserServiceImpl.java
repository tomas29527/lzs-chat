package com.lzs.chat.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzs.chat.service.entity.User;
import com.lzs.chat.service.mapper.UserMapper;
import com.lzs.chat.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/27 11:25
 * @since <版本号>
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User getUserBiUserId(String userId) {
        QueryWrapper<User> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        return userMapper.selectOne(queryWrapper);
    }
}
