package com.mflyyou.cloud.sdk;

import com.mflyyou.cloud.sdk.request.CreateOrderRequest;
import com.mflyyou.cloud.sdk.response.CreateOrderResponse;

public interface OrderApi {
    CreateOrderResponse create(CreateOrderRequest createOrderRequest);
}
