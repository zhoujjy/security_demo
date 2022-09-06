package com.zj.security_demo.controller;

import com.zj.security_demo.common.R;
import com.zj.security_demo.entity.User;
import com.zj.security_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R login(@RequestBody User user){
        return userService.login(user);
    }

    @PostMapping("/logout")
    public R logout(){
        return userService.logout();
    }

    @PreAuthorize("hasAuthority({'user:dd'})")
    @GetMapping("/test")
    public String test(){
        return "test";
    }

}
