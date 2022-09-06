package com.zj.security_demo.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zj.security_demo.common.R;
import com.zj.security_demo.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //给前端ResponseResult 的json
//        R r = new R(HttpStatus.UNAUTHORIZED.value(), "登陆认证失败了，请重新登陆！");
        R r = new R(HttpStatus.UNAUTHORIZED.value(), authException.getMessage());
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(r);
        WebUtils.renderString(response,json);
    }
}