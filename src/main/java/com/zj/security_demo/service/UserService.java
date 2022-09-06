package com.zj.security_demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zj.security_demo.common.R;
import com.zj.security_demo.entity.User;

public interface UserService  extends IService<User> {
    R login(User user);

    R logout();
}
