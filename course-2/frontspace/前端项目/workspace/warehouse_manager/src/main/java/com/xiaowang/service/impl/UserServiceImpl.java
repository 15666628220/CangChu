package com.xiaowang.service.impl;

import com.xiaowang.mapper.UserMapper;
import com.xiaowang.pojo.User;
import com.xiaowang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 小王
 **/

@Service
public class UserServiceImpl implements UserService {
    //注入userMapper
    @Autowired
    private UserMapper userMapper;
    @Override
    public User queryUserByCode(String userCode) {
        return userMapper.findUserByCode(userCode);
    }
}
