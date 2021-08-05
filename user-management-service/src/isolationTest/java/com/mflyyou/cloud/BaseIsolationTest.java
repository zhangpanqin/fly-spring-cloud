package com.mflyyou.cloud;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.config.EncoderConfig;
import io.restassured.config.LogConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import io.restassured.path.json.mapper.factory.Jackson2ObjectMapperFactory;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureEmbeddedDatabase
@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseIsolationTest {

    @Autowired
    protected WebApplicationContext context;

    @BeforeEach
    public void init() {
        RestAssuredMockMvc.webAppContextSetup(context);
    }

    @AfterEach
    public void reset() {
        RestAssuredMockMvc.reset();
    }

    @Autowired
    protected ObjectMapper objectMapper;

    protected MockMvcRequestSpecification given() {
        return RestAssuredMockMvc.given()
                .config(RestAssuredMockMvcConfig.config()
                        .encoderConfig(new EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false))
                        .objectMapperConfig(ObjectMapperConfig.objectMapperConfig().jackson2ObjectMapperFactory(getJackson2ObjectMapperFactory()))
                        .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    private Jackson2ObjectMapperFactory getJackson2ObjectMapperFactory() {
        return (cls, charset) -> objectMapper;
    }
}