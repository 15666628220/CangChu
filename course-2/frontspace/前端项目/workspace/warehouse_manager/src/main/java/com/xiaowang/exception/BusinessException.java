package com.xiaowang.exception;

/**
 * @author 小王
 * 自定义的运行时异常
 **/
public class BusinessException extends RuntimeException{
   //创建异常对象
    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }
}
