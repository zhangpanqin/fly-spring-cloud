package com.mflyyou.cloud.order;

import com.mflyyou.cloud.common.exception.AccessDeniedAppException;
import com.mflyyou.cloud.sdk.OrderApi;
import com.mflyyou.cloud.sdk.request.CreateOrderRequest;
import com.mflyyou.cloud.sdk.response.CreateOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController implements OrderApi {

    private final OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    public OrderController(OrderService orderService, ScopeTest scopeTest) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderResponse create(@RequestBody CreateOrderRequest createOrderRequest) {
        return orderService.create(createOrderRequest);
    }

    @GetMapping("/test")
    public String test3() {
        orderService.refreshScopeInfo();
        return "success";
    }

    @GetMapping("/get/{date}")
    public DateDTO test(@PathVariable LocalDate date, LocalDateTime localDateTime, Instant instant) {
        return DateDTO.builder()
                .localDate(date)
                .localDateTime(localDateTime)
                .instant(instant)
                .build();
    }

    /**
     * curl -X POST -d '{"localDate": "2021-12-12","localDateTime": "2021-12-12 22:22:22","instant": 1628653.018}' -H "Content-Type: application/json" http://localhost:8080/orders/post
     */

    @PostMapping("/post")
    public DateDTO test2(@RequestBody DateDTO dateDTO) {
        return dateDTO;
    }

    @PostMapping("/post2")
    // form-data 和 x-www-form-urlencoded query string 都可以
    public DateDTO test222(DateDTO dateDTO) {
        System.out.println(httpServletRequest.getQueryString());
        return dateDTO;
    }

    @GetMapping("/ex")
    public DateDTO test23() {
        if (true) {
            throw new AccessDeniedAppException(Map.of("test", "测试"));
        }
        return DateDTO.builder().build();
    }
}
