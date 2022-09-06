package com.zj.security_demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zj.security_demo.common.R;
import com.zj.security_demo.entity.LoginUser;
import com.zj.security_demo.entity.User;
import com.zj.security_demo.exception.BusinessException;
import com.zj.security_demo.mapper.UserMapper;
import com.zj.security_demo.service.UserService;
import com.zj.security_demo.utils.JwtUtil;
import com.zj.security_demo.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.zj.security_demo.common.ErrCodeEnume.USERNAMEORPASSWORD_ERR;
import static com.zj.security_demo.common.RedisConstants.USER_LOGIN_KEY;
import static com.zj.security_demo.common.RedisConstants.USER_LOGIN_TTL;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Override
    public R login(User user) {
        //使用ProviderManager auth方法进行验证
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        //校验失败了
        if(Objects.isNull(authenticate)){
            throw new BusinessException(USERNAMEORPASSWORD_ERR.getCode(),USERNAMEORPASSWORD_ERR.getMsg());
        }

        //4自己生成jwt给前端
        LoginUser loginUser= (LoginUser)(authenticate.getPrincipal());
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        Map<String,String> map=new HashMap<>();
        map.put("token",jwt);

        //5系统用户相关所有信息放入redis


        redisUtils.setObject(USER_LOGIN_KEY+userId,loginUser,USER_LOGIN_TTL, TimeUnit.MINUTES);


        return new R(200,"登陆成功",map);
    }

    @Override
    public R logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
        redisUtils.removeValue(USER_LOGIN_KEY+userId);
        return new R(200,"退出成功！");
    }
}
