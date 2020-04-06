package com.imooc.controller.interceptor;

import com.imooc.utils.IMoocJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class MiniInterceptor implements HandlerInterceptor {

    @Autowired
    public RedisOperator redis;

    public static final String USER_REDIS_SESSION = "user-redis-session";

    /**
     * 对请求做判断, 请求controller之前
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        String userId = request.getHeader("userId");
        String userToken = request.getHeader("userToken");

        /**
         * false: 请求被拦截, 返回
         * true: 请求ok, 继续执行
         * 对所有已配置的接口请求都会进行拦截验证, 接口配置在WebMvcConfig中
         */
        // 前端Token不为空
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userToken)){
            String uniqueToken = redis.get(USER_REDIS_SESSION + ":" + userId);
            // 前端有Token, 但是用户登录信息已过期, 后端Token被抹掉, 需要重新登录
            if (StringUtils.isEmpty(uniqueToken) && StringUtils.isBlank(uniqueToken)){
                System.out.println("请登录...");
                returnErrorResponse(response,IMoocJSONResult.errorTokenMsg("请登录..."));
                return false;
            } else {
                // 当前用户在另外一台设备上登录，后端Token已被更改, 当前userToken失效
                if (!userToken.equals(uniqueToken)){
                    System.out.println("账号被挤出...");
                    returnErrorResponse(response,IMoocJSONResult.errorTokenMsg("账号被挤出..."));
                    return false;
                }
            }
        // 前端Token为空
        } else {
            System.out.println("请登录...");
            returnErrorResponse(response,IMoocJSONResult.errorTokenMsg("请登录..."));
            return false;
        }
        return true;
    }

    public void returnErrorResponse(HttpServletResponse response, IMoocJSONResult result)
            throws IOException, UnsupportedEncodingException {
        OutputStream out=null;
        try{
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            out = response.getOutputStream();
            out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            out.flush();
        } finally{
            if(out!=null){
                out.close();
            }
        }
    }

    /**
     * 请求controller之后, 渲染视图之前
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 请求controller之后
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
