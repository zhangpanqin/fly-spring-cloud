package com.mflyyou.cloud.common;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.mflyyou.cloud.common.constant.TimeFormatterConstant.DATE_FORMATTER;
import static com.mflyyou.cloud.common.constant.TimeFormatterConstant.DATE_TIME_FORMATTER;
import static com.mflyyou.cloud.common.constant.TimeFormatterConstant.TIME_FORMATTER;

/**
 * 因为 SpringBootApplication 标记的类,会自动扫描当前包及及子包的配置,为了避免包名不同加上此配置
 * <p>
 * jackson 日期反序列化和序列化
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Jackson2ObjectMapperBuilder.class)
public class CommonJacksonCustomizerAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    static class Jackson2CustomizerConfig {
        @Bean
        public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
            return builder -> {
                // 序列化
                builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER));
                builder.serializerByType(LocalDate.class, new LocalDateSerializer(DATE_FORMATTER));
                builder.serializerByType(LocalTime.class, new LocalTimeSerializer(TIME_FORMATTER));
                builder.serializerByType(Instant.class, InstantSerializer.INSTANCE);


                // 反序列化
                builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER));
                builder.deserializerByType(LocalDate.class, new LocalDateDeserializer(DATE_FORMATTER));
                builder.deserializerByType(LocalTime.class, new LocalTimeDeserializer(TIME_FORMATTER));
                builder.deserializerByType(Instant.class, InstantDeserializer.INSTANT);

                // 配置 http 返回的 json 数据 中 Instant,为秒值
                builder.postConfigurer(objectMapper -> {
                    objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
                    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
                });
            };
        }
    }
}
