package com.zj.security_demo;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
class SecurityDemoApplicationTests {

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;
    @Test
    void contextLoads() {


        String encode = bCryptPasswordEncoder.encode("123456");
        System.out.println(encode);
    }

}
