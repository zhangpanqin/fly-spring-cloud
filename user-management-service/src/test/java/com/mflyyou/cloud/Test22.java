package com.mflyyou.cloud;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class Test22 {
    @Test
    void test() {
        System.out.println("aa");
        assertThat("aaa", is("aaa"));
    }
}
