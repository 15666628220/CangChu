package com.xiaowang.mapper;

import com.xiaowang.pojo.User;


/**
 * @author 小王
 * user_info的mapper接口
 **/

public interface UserMapper {
    //根据账号查询信息的方法
//    public User findUserByCode(String userCode);
    public User findUserByCode(String userCode);
}
