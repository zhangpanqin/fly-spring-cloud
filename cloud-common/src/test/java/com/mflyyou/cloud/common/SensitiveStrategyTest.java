package com.mflyyou.cloud.common;

import com.github.houbb.sensitive.core.api.SensitiveUtil;
import org.junit.jupiter.api.Test;

public class SensitiveStrategyTest {
    @Test
    public void UserSensitiveTest() {
        User user = buildUser();
        System.out.println("脱敏前原始： " + user);
        User sensitiveUser = SensitiveUtil.desCopy(user);
        System.out.println("脱敏对象： " + sensitiveUser);
        System.out.println("脱敏后原始： " + user);
    }

    private User buildUser() {
        User user = new User();
        user.setUsername("1234567");
        user.setPassword("123456");
        user.setEmail("12345@qq.com");
        user.setIdCard("123456190001011234");
        user.setPhone("18888888888");
        return user;
    }
}
