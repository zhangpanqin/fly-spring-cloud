package com.mflyyou.cloud.order;

import org.springframework.beans.factory.InitializingBean;

public class TestInit implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(111);
    }
}
