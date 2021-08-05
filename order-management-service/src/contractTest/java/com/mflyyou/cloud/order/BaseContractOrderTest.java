package com.mflyyou.cloud.order;

import com.mflyyou.cloud.BaseContractTest;
import com.mflyyou.cloud.sdk.request.CreateOrderRequest;
import com.mflyyou.cloud.sdk.response.CreateOrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BaseContractOrderTest extends BaseContractTest {

    @Autowired
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        create();
    }

    private void create() {
        when(orderService.create(any(CreateOrderRequest.class)))
                .thenReturn(CreateOrderResponse.builder()
                        .id(1L)
                        .build());
    }
}
