package com.xiaowang.config;

import com.xiaowang.filter.LoginCheckFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;

/**
 * @author 小王
 * 原生Servlet配置类
 **/
@Configuration
public class ServletConfig {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        //创建FilterRegistrationBean的@Bean对象
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        //创建自定义过滤器
        LoginCheckFilter loginCheckFilter = new LoginCheckFilter();
        //创建Redis模板.见给redis
        loginCheckFilter.setRedisTemplate(redisTemplate);
        //将自定义的过滤器注册到filterFilterRegistrationBean中
        filterRegistrationBean.setFilter(loginCheckFilter);
        //给过滤器指定拦截的请求
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}
