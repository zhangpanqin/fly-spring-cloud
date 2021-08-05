package com.mflyyou.cloud;

import io.restassured.config.EncoderConfig;
import io.restassured.config.LogConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureStubRunner
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
public abstract class BaseContractTest {

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @BeforeEach
    public void init() {
        RestAssuredMockMvc.webAppContextSetup(this.webApplicationContext);
    }

    @AfterEach
    public void reset() {
        RestAssuredMockMvc.reset();
    }

    public MockMvcRequestSpecification given() {
        return RestAssuredMockMvc.given()
                .config(RestAssuredMockMvcConfig.config().encoderConfig(new EncoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .config(RestAssuredMockMvcConfig.config()
                        .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails()));
    }
}
