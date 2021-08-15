package com.mflyyou.cloud.user;

import brave.Tracer;
import com.mflyyou.cloud.common.lock.DistributedLock;
import com.mflyyou.cloud.sdk.request.CreateOrderRequest;
import com.mflyyou.cloud.sdk.request.CreateUserRequest;
import com.mflyyou.cloud.sdk.response.CreateOrderResponse;
import com.mflyyou.cloud.sdk.response.CreateUserResponse;
import com.mflyyou.cloud.sdk.response.GetUserResponse;
import com.mflyyou.cloud.sdk.response.UserResponse;
import com.mflyyou.cloud.user.aop.Order1;
import com.mflyyou.cloud.user.aop.Order2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserService {
    private static final String CACHE_NAME = "REDIS_USER_INFO";
    private final Tracer tracer;
    private final OrderClient orderClient;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final PlatformTransactionManager ptm;

    public UserService(Tracer tracer, OrderClient orderClient, UserRepository userRepository, RedisTemplate<String, String> redisTemplate, PlatformTransactionManager ptm, ApplicationContext applicationContext) {
        this.tracer = tracer;
        this.orderClient = orderClient;
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
        this.ptm = ptm;
    }

    public UserResponse getUser(Long userId) {
        log.info("traceId: {}", tracer.currentSpan().context().traceIdString());
        CreateOrderResponse createOrderResponse = orderClient.create(CreateOrderRequest.builder()
                .userId(userId)
                .build());
        return UserResponse.builder()
                .userId(userId)
                .orderId(createOrderResponse.getId())
                .build();
    }

    @Transactional
    @DistributedLock(value = "#{#createUserRequest.username}", leaseTime = -1L)
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(ThreadLocalRandom.current().nextLong());
        userEntity.setUsername(createUserRequest.getUsername());
        userRepository.save(userEntity);
        System.out.println(userEntity.getId());
        try {
            if (Long.valueOf(createUserRequest.getUsername()).equals(11L)) {
                TimeUnit.SECONDS.sleep(20);
            }
        } catch (Exception e) {
            throw new RuntimeException("报错了");
        }
        return CreateUserResponse.builder()
                .id(userEntity.getId())
                .build();
    }

    @Cacheable(value = CACHE_NAME, key = "#id")
    @DistributedLock(value = "#{#id}", leaseTime = -1L)
    public GetUserResponse getUserInfo(Long id) {
        log.info("执行了,{}", redisTemplate.opsForValue().get("key22"));

        return userRepository.findById(id).map(userEntity -> GetUserResponse.builder()
                .username(userEntity.getUsername())
                .id(userEntity.getId())
                .build())
                .orElseThrow(() -> new RuntimeException("查询的数据不存在"));
    }

    @Order1
    @Order2
    public GetUserResponse aop() {

        System.out.println("UserService.aop");

        return GetUserResponse.builder().username("111").build();
    }
}
