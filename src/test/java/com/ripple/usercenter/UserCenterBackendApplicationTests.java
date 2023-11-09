package com.ripple.usercenter;

import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;


//@SpringBootTest
class UserCenterBackendApplicationTests {

    @Test
    void contextLoads() {
    }

    // 测试加密
    @Test
    void testDigest() {
        // 使用Spring提供的加密工具类
        String result = DigestUtils.md5DigestAsHex("abdc12345".getBytes());
        System.out.println(result);

    }
}
