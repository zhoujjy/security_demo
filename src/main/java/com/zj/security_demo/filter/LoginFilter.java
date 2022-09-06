package com.zj.security_demo.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zj.security_demo.common.BaseContext;
import com.zj.security_demo.entity.LoginUser;
import com.zj.security_demo.exception.BusinessException;
import com.zj.security_demo.utils.JwtUtil;
import com.zj.security_demo.utils.RedisUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.zj.security_demo.common.ErrCodeEnume.TOKEN_ERR;
import static com.zj.security_demo.common.ErrCodeEnume.USERNOTLOGIN_ERR;
import static com.zj.security_demo.common.RedisConstants.USER_LOGIN_KEY;
import static com.zj.security_demo.common.RedisConstants.USER_LOGIN_TTL;

@Component
public class LoginFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1获取token  header的token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //放行，让后面的过滤器执行
            filterChain.doFilter(request, response);
            return;
        }

        //2解析token
        String userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            throw new BusinessException(TOKEN_ERR.getCode(),TOKEN_ERR.getMsg());
        }

        //3获取userId, redis获取用户信息
        LoginUser loginUser = null;
        try {
            loginUser = redisUtils.getObject(USER_LOGIN_KEY+userId, LoginUser.class);
        } catch (JsonProcessingException e) {
            throw new BusinessException(USERNOTLOGIN_ERR.getCode(),USERNOTLOGIN_ERR.getMsg());
        }

        //4封装Authentication
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());

        //5存入SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        //将userid保存到线程中
        BaseContext.setCurrentId(Long.valueOf(userId));

        //刷新时间
        redisUtils.setTime(USER_LOGIN_KEY+userId,USER_LOGIN_TTL, TimeUnit.MINUTES);

        //放行，让后面的过滤器执行
        filterChain.doFilter(request, response);
    }
}
