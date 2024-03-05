package com.xiaowang.filter;

import com.alibaba.fastjson.JSON;
import com.xiaowang.pojo.Result;
import com.xiaowang.utils.WarehouseConstants;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.LogRecord;

/**
 * @author 小王
 **/
public class LoginCheckFilter implements Filter {

    private StringRedisTemplate redisTemplate;

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        //进行转换（在这里为什么要转换）
        HttpServletRequest request=(HttpServletRequest) req;
        HttpServletResponse response=(HttpServletResponse) resp;

            //白名单请求直接放行
        List<String > urlList=new ArrayList<>();
        urlList.add("/captcha/captchaImage");
        urlList.add("/login");
        //拦截器拦截到当前请求的url接口
        String url = request.getServletPath();
        if (urlList.contains(url)){
            chain.doFilter(request,response);
            return;
        }
        //其他请求校验是否采用token，判断reids中是否存在redis的键
        String token = request.getHeader(WarehouseConstants.HEADER_TOKEN_NAME);
       //是否由文本
        if(StringUtils.hasText(token)&&redisTemplate.hasKey(token)){
         chain.doFilter(request,response);
        }
//        //没有，说明未登录或token
//        HashMap<String , Object> data = new HashMap<>();
//        data.put("code",401);
//        data.put("message","尚未登录");
        Result result = Result.err(Result.CODE_ERR_UNLOGINED, "你没有登录");
        String jsonStr= JSON.toJSONString("application/json;charset=UTF-8");
        PrintWriter out=response.getWriter();
        out.write(jsonStr);
        out.flush();
    }
}
