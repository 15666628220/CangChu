package com.xiaowang.service;

import com.xiaowang.pojo.User;

/**
 * @author 小王
 * user_info的service接口
 **/
public interface UserService {
    //根据账号查询业务方法
    public User queryUserByCode(String userCode);
}
