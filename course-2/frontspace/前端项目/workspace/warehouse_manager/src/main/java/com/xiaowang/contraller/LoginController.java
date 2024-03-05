package com.xiaowang.contraller;

import com.google.code.kaptcha.Producer;
import com.xiaowang.pojo.CurrentUser;
import com.xiaowang.pojo.LoginUser;
import com.xiaowang.pojo.Result;
import com.xiaowang.pojo.User;
import com.xiaowang.service.UserService;
import com.xiaowang.utils.DigestUtil;
import com.xiaowang.utils.TokenUtils;
import com.xiaowang.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author 小王
 **/
@RestController //代表控制器类，将控制器类@ResponseBody注解
public class LoginController {
    //注入DefaultKaptcha的bean对象 ----生成验证码图片
    @Autowired //类型自动注入
//    @Resource(name="captchaProducer")//按照名字进行注解
        private Producer producer;//Producer是一个接口，里边实现了由一个实现类DefaultKaptcha
    //注入redis模板
    @Autowired  //注入redis模板
    private StringRedisTemplate redisTemplate;
    //生成用户登录验证码
    @RequestMapping("/captcha/captchaImage")
    public void captchaImage(HttpServletResponse response){
       //字节输出流
        ServletOutputStream out=null;
        try {
        //生成验证码图片所需要的文字
        String text = producer.createText();
        //使用验证码文本生成验证码文本 BufferedImage缓冲图片，在内存里
        BufferedImage image = producer.createImage(text);
        //将验证码文本箭保存到redis redisTemplate.opsForValue()，opsForValue()操作器：键值 60*30 设置键的持续时间为30分钟.SECONDS单位时间为秒

        redisTemplate.opsForValue().set(text,"",60*30, TimeUnit.SECONDS);
        /*
        * 将验证码生成给前端
        * setContentType响应头
        * */
//        设置响应正文image/jpeg
        response.setContentType("image/jpeg");
        //将验证码图片写给前端
            out = response.getOutputStream();
            ImageIO.write(image,"jpg",out);//使用响应对象的字节输出写入验证码的图片，自然是给终端写入
            //刷新
            out.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if (out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }
//    注入UserService
    @Autowired
    private UserService userService;
    //注入tokenUtis对象
    @Autowired
    private TokenUtils tokenUtils;

    //注入redis模板
//    @Autowired
//    private RedisTemplate redisTemplate;
    @RequestMapping("/login")
    //@RequestBody LoginUser loginUser表示接受传递前端登录用户的json的字符串
    //返回值为Result对象，表示像前端响应结果对象转换的json串，
    public Result login(@RequestBody LoginUser loginUser){
        //拿到用户所需要的验证码
        String code = loginUser.getVerificationCode();
        if (!redisTemplate.hasKey(code)) {
            return Result.err(Result.CODE_ERR_BUSINESS,"验证码错误");
        }
        //根据账号查询用户
        User user = userService.queryUserByCode(loginUser.getUserCode());
        if (user!=null){
                //账号存在
            if (user.getUserState().equals(WarehouseConstants.USER_STATE_PASS)){
                //校验密码
                //拿到用户录入的密码
                String userPwd=loginUser.getUserPwd();
                //进行DM5加密
                userPwd = DigestUtil.hmacSign(userPwd);
                if (userPwd.equals(user.getUserPwd())){//密码合法
                    //生程jwk token
                    //创建CurrentUser，封装账户id，账户姓名
                    CurrentUser currentUser = new CurrentUser(user.getUserId(),user.getUserCode(),user.getUserName());
                    //工具类的loginSign方法
                    String token = tokenUtils.loginSign(currentUser, userPwd); //redis中也存在
                    //向客户端响应jwt token
                    return Result.ok("登录成功",token);

                }
                else {
                    //密码不合法
                    return Result.err(Result.CODE_ERR_BUSINESS,"密码错误");
                }


            }else {
                return Result.err(Result.CODE_ERR_BUSINESS,"用户未审核");
            }
        }
        else {
            //账号不存在
            return Result.err(Result.CODE_ERR_BUSINESS,"账号不存在");
        }
    }
    //g获取当前信息的url接口
    //@RequestHeade获取请求头
    @RequestMapping("/curr-user")
    public Result CurrenUser(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        //解析token，拿到了封装在当前对象的信息的currentUser对象
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        return Result.ok(currentUser);
    }


}
