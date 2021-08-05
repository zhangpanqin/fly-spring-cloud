package com.mflyyou.cloud.order;

import brave.Tracer;
import com.mflyyou.cloud.sdk.request.CreateOrderRequest;
import com.mflyyou.cloud.sdk.response.CreateOrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class OrderService {
    private final Tracer tracer;
    private final ApplicationContext applicationContext;
    @Autowired
    private ScopeTest scopeTest;

    public OrderService(Tracer tracer, ApplicationContext applicationContext) {
        this.tracer = tracer;
        this.applicationContext = applicationContext;
    }

    public CreateOrderResponse create(CreateOrderRequest request) {
        log.info("traceId: {}", tracer.currentSpan().context().traceIdString());
        if (request.getUserId()!=null && request.getUserId() % 2==0) {
            throw new RuntimeException("故意抛出异常");
        }
        return CreateOrderResponse.builder()
                .id(2L)
                .build();
    }

    public void testS() {
        System.out.println(applicationContext);
        System.out.println(applicationContext.getParent());
        System.out.println(scopeTest);
    }
}
