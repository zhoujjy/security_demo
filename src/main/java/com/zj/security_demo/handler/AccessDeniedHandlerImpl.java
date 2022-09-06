package com.zj.security_demo.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zj.security_demo.common.R;
import com.zj.security_demo.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //给前端ResponseResult 的json
        R r = new R(HttpStatus.FORBIDDEN.value(), "您权限不足！");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(r);
        WebUtils.renderString(response,json);
    }
}