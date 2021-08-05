### 查看 nexus 密码

```shell
docker exec fly-spring-contract-nexus3 cat /nexus-data/admin.password
```



### zipkin

```shell
curl -s -I -X GET http://localhost:8081/users/221
```



### grafna

admin/admin



堆和非堆内存有以下几个概念：

#### init

表示JVM在启动时从操作系统申请内存管理的初始内存大小(以字节为单位)。JVM可能从操作系统请求额外的内存，也可以随着时间的推移向操作系统释放内存（经实际测试，这个内存并没有过主动释放）。这个init的值可能不会定义。

#### used

表示当前使用的内存量(以字节为单位)

#### committed

表示保证可供 Jvm使用的内存大小(以字节为单位)。 已提交内存的大小可能随时间而变化(增加或减少)。 JVM也可能向系统释放内存，导致已提交的内存可能小于 init，但是committed永远会大于等于used。

#### max

表示可用于内存管理的最大内存(以字节为单位)。

committed不足时jvm向系统申请，若超过max则发生OutOfMemoryError错误。

[美团垃圾回收讲解](https://tech.meituan.com/2020/11/12/java-9-cms-gc.html)

[used,committed,max介绍](https://docs.oracle.com/javase/7/docs/api/java/lang/management/MemoryUsage.html)

### docker

```txt
容易通过下面的地址可以访问宿主机
host.docker.internal
```



### prometheus

修改 `fly-spring-cloud/gradle/docker-compose/prometheus/prometheus.yml` 配置，重启 prometheus 容器就可以加载新添加的配置

```yml
global:
  scrape_interval: 15s
  scrape_timeout: 10s
  evaluation_interval: 15s
alerting:
  alertmanagers:
  - follow_redirects: true
    scheme: http
    timeout: 10s
    api_version: v2
    static_configs:
    - targets: []
scrape_configs:
- job_name: prometheus
  honor_timestamps: true
  scrape_interval: 15s
  scrape_timeout: 10s
  metrics_path: /metrics
  scheme: http
  follow_redirects: true
  static_configs:
  - targets:
    - localhost:9090
- job_name: order-management-service
  honor_timestamps: true
  scrape_interval: 15s
  scrape_timeout: 10s
  metrics_path: /actuator/prometheus
  scheme: http
  follow_redirects: true
  static_configs:
  - targets:
    - host.docker.internal:8080
```

在 spring boot 服务商 http://localhost:{server.port}/actuator/prometheus

多次请求跑数据

```shell
ab -n 10000000 -c 200 -s 20000  http://localhost:8081/users/223
```



### spring cloud config

actuator 配置的信息

```txt
curl -X POST -d {} -H "Content-Type: application/json" http://localhost:8080/actuator/refresh
```

当手动请求了 `/actuator/refresh` 之后

ConfigurationProperties 标记的配置类是可以自动刷新的，不需要添加 `@RefreshScope` 注解

```java
@Configuration
@Data
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private String confi_test = "默认值";
}
```



当手动请求了 `/actuator/refresh` 之后

@Value 标记的值是不可以被刷新的

```java
@SpringBootApplication
@EnableScheduling
public class OrderManagementServiceApplication {
    @Autowired
    private AppConfig appConfig;

    @Value("${app.confi_test}")
    private String test2;

    public static void main(String[] args) {
        SpringApplication.run(OrderManagementServiceApplication.class, args);
    }

    @Scheduled(fixedDelay = 5000)
    public void run() {
        System.out.println(appConfig.getConfi_test());
        System.out.println("----------------");
        System.out.println(test2);

    }
}
```

```
EnvironmentChangeEvent
```

refresh 会触发 bean 的销毁和创建逻辑
