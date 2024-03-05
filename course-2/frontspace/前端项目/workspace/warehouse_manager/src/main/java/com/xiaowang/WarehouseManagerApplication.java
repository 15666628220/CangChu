package com.xiaowang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

//@MapperScan 指名mapper接口所在的包，然后自动为mapper接口创建我们的代理的对象，并加入到IOC容器
@Configuration
@MapperScan(basePackages="com.xiaowang.mapper")
@SpringBootApplication
public class WarehouseManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarehouseManagerApplication.class);
    }

}
