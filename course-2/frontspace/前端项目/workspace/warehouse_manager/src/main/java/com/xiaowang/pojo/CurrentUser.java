package com.xiaowang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 此User类只封装了用户的用户id、用户名和真实姓名
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CurrentUser {  //当前用户

    private int userId;//用户id

    private String userCode;//用户名

    private String userName;//真实姓名
}
