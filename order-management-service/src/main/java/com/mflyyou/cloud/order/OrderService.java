package com.mflyyou.cloud.order;

import com.mflyyou.cloud.common.lock.DistributedLock;
import com.mflyyou.cloud.sdk.request.CreateOrderRequest;
import com.mflyyou.cloud.sdk.response.CreateOrderResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
@RefreshScope
public class OrderService {

    @Autowired
    private ApplicationContext applicationContext;

    public CreateOrderResponse create(CreateOrderRequest request) {
        if (request.getUserId()!=null && request.getUserId() % 2==0) {
            throw new RuntimeException("故意抛出异常");
        }
        return CreateOrderResponse.builder()
                .id(2L)
                .build();
    }

    public void refreshScopeInfo() {
        System.out.println(applicationContext.getBean("orderService"));
        System.out.println("study refresh scope");
    }

    @SneakyThrows
    @DistributedLock(value = "#id",leaseTime = 10_000L)
    public Long lock(Long id) {
        if (Objects.equals(1L, id)) {
            TimeUnit.SECONDS.sleep(30);
        }
        System.out.println("执行完了");
        return id;
    }
}
