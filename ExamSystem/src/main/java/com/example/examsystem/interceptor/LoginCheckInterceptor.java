package com.example.examsystem.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.example.examsystem.dto.Response;
import com.example.examsystem.dto.ResponseEnum;
import com.example.examsystem.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    //ctrl+o
    @Override   //目标方法运行前运行，返回true：放行
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取请求url
        String url = request.getRequestURL().toString();
        //2.判断url是login则放行
        if(url.contains("login")){//有漏洞：被恶意构造?login=...
            return true;
        }
        //3.获取请求头的令牌
        String token = request.getHeader("token");
        //4.判断令牌是否为空
        if(token == null){
            log.info("token为空");
            Response response1 = new Response(ResponseEnum.Login_Failure);
            String notLogin = JSONObject.toJSONString(response1);   //调用alibaba的JSONObject
            response.getWriter().write(notLogin);
            return false;
        }
        //5.判断令牌是否正确
        try {
            JwtUtils.parseJWT(token);
        } catch (Exception e) {
            log.info("token解析失败，返回未登录错误信息");
            Response response1 = new Response(ResponseEnum.Login_Failure);
            String notLogin = JSONObject.toJSONString(response1);   //调用alibaba的JSONObject
            response.getWriter().write(notLogin);
            return false;
        }

        //6.放行
        return true;

    }

    @Override   //目标方法运行后运行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override   //视图渲染完毕后运行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
