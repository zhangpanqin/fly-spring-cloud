package com.mflyyou.cloud.user;

import com.mflyyou.cloud.sdk.OrderApi;
import com.mflyyou.cloud.sdk.request.CreateOrderRequest;
import com.mflyyou.cloud.sdk.response.CreateOrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order-client",
        url = "${app.feignclient.order-management-url}",
        primary = false)
public interface OrderClient extends OrderApi {
    @Override
    @PostMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    CreateOrderResponse create(@RequestBody CreateOrderRequest createOrderRequest);
}
