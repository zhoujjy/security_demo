package com.zj.security_demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zj.security_demo.entity.LoginUser;
import com.zj.security_demo.entity.User;
import com.zj.security_demo.exception.BusinessException;
import com.zj.security_demo.mapper.MenuMapper;
import com.zj.security_demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.zj.security_demo.common.ErrCodeEnume.USERNAME_ERR;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(wrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        if(Objects.isNull(user)){
            throw new BusinessException(USERNAME_ERR.getCode(),USERNAME_ERR.getMsg());
        }
        // 查询权限信息
        List<String> perms = menuMapper.selectPermsByUserId(user.getId());

        //3返回UserDetails
        return new LoginUser(user,perms);
    }
}
