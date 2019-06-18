package com.mmall.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Author: Zhang Yan
 * Date: 19-6-13 下午5:38
 * Content:
 */
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        log.info("preHandle");
        //请求Controller中的方法名
        HandlerMethod handlerMethod = (HandlerMethod)o;

        //解析handlerMethod

        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();

        //解析参数，具体的参数key 以及value打印日志

        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = httpServletRequest.getParameterMap();
        Iterator iterator = paramMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            String mapKey = (String)entry.getKey();

            String mapValue = StringUtils.EMPTY;

            //request这个参数，里面的value是一个string数组 String[]

            Object object = entry.getValue();
            if (object instanceof String[]){
                String[] strings = (String[])object;
                mapValue = Arrays.toString(strings);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
            log.info(requestParamBuffer.toString());
        }

        if (StringUtils.equals(className, "UserManageController") && StringUtils.equals(methodName, "login")){
            log.info("权限拦截器拦截到请求，className:{},methodName:{}",className,methodName);
            //如果是拦截到登录请求，不打印参数，因为参数里有密码，全部打印到日志中防止泄露
            return true;
        }
        log.info("权限拦截器拦截到请求，className:{},methodName:{}，param:{}",className,methodName,requestParamBuffer.toString());

        User user = null;

        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.string2Obj(userJsonStr,User.class);
        }

        if (user == null || (user.getRole().intValue() != Const.Role.ROLE_ADMIN)){
            //返回false，即不会调用controller 的方法
            httpServletResponse.reset(); //这里要添加reset, 否则报异常getWriter() has already been called for this response
            httpServletResponse.setCharacterEncoding("UTF-8"); //这里要设置编码，否则乱码
            httpServletResponse.setContentType("application/json;charset=UTF-8");// 这里要设置返回值的类型，因为全部是JSON类型

            PrintWriter out = httpServletResponse.getWriter();

            //上传由于富文本的返回值的特殊要求，要特殊处理返回值
            if (user == null){

                if (StringUtils.equals(className, "ProductManageController") && StringUtils.equals(methodName, "richtextImgUpload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "请登录管理员");
                    out.print(JsonUtil.obj2String(resultMap));
                }else {
                    out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户未登录")));
                }
            }else {
                if (StringUtils.equals(className, "ProductManageController") && StringUtils.equals(methodName, "richtextImgUpload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "无权限操作");
                    out.print(JsonUtil.obj2String(resultMap));
                }else {
                    out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("用户无权限操作")));
                }
            }
            out.flush();
            out.close(); //这里要关闭

            return false;
        }


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        log.info("afterCompletion");
    }
}
