package com.zj.security_demo.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zj.security_demo.common.R;
import com.zj.security_demo.exception.BusinessException;
import com.zj.security_demo.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.zj.security_demo.common.ErrCodeEnume.UNKNOW_EXCEPTION;

@Component
public class ExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request,response);
        }catch (BusinessException e){
            //给前端ResponseResult 的json
            R r = new R(e.getCode(),e.getMessage());
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(r);
            WebUtils.renderString(response,json);
        }catch (Throwable throwable){
            throwable.printStackTrace();

            R r = new R(UNKNOW_EXCEPTION.getCode(),UNKNOW_EXCEPTION.getMsg());
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(r);
            WebUtils.renderString(response,json);
        }
    }
}
