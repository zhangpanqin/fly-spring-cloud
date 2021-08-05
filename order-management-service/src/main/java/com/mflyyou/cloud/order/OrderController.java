package com.mflyyou.cloud.order;

import com.mflyyou.cloud.sdk.OrderApi;
import com.mflyyou.cloud.sdk.request.CreateOrderRequest;
import com.mflyyou.cloud.sdk.response.CreateOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@RestController
@RequestMapping("/orders")
@RefreshScope
public class OrderController implements OrderApi {

    private final OrderService orderService;

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private AppConfig appConfig;


    public OrderController(OrderService orderService, ScopeTest scopeTest) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderResponse create(@RequestBody CreateOrderRequest createOrderRequest) {
        return orderService.create(createOrderRequest);
    }

    @GetMapping("/test")
    public String test() {
        System.out.println("--------orderService 中----------");
        orderService.testS();

        System.out.println("--------OrderController.applicationContext.getBean(ScopeTest.class)----------");
        ScopeTest bean = applicationContext.getBean(ScopeTest.class);
        System.out.println(bean);
        bean.aa();
        System.out.println("--------OrderController.applicationContext.getBean(ScopeTest.class)----------");
        ScopeTest bean2 = applicationContext.getBean(ScopeTest.class);
        System.out.println(bean2);
        bean2.aa();

        orderService.testS();
        return "success";
    }

    @GetMapping("/test3")
    public String test3() {
        System.out.println(applicationContext);
        orderService.testS();
        return "success";
    }

    @GetMapping("/test2")
    public String test2() {
        System.out.println(appConfig);
        AppConfig bean = applicationContext.getBean(AppConfig.class);
        System.out.println(bean);
        bean.test();
        OrderController orderController = applicationContext.getBean(OrderController.class);
        System.out.println(this);
        System.out.println(String.format("------------orderController" + orderController));
        return "success";
    }

    @PreDestroy
    public void des() {
        System.out.println("order 销毁了");
    }

    @PostConstruct
    public void init() {
        System.out.println("order 创建了");
    }
}
