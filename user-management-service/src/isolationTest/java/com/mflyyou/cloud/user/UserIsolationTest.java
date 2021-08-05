package com.mflyyou.cloud.user;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.mflyyou.cloud.BaseIsolationTest;
import com.mflyyou.cloud.sdk.request.CreateUserRequest;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
public class UserIsolationTest extends BaseIsolationTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    void should_success_create_user() {
        MockMvcResponse response = given()
                .body(CreateUserRequest.builder()
                        .username("aaaa")
                        .build())
                .post("/users");

        assertThat(response.statusCode(), is(HttpStatus.OK.value()));
        DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
        assertThat(parsedJson.read("$.id"), notNullValue());

        List<UserEntity> all = userRepository.findAll();
        assertThat(all, contains(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("username", is("aaaa"))
        )));
    }
}