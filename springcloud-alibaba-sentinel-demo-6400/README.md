[Controller](./src/main/java/com/example/springcloud/alibaba/controller/SentinelDemoController.java) 定义两个
Endpoint，调用了同一个 [Service 方法](./src/main/java/com/example/springcloud/alibaba/service/impl/GoodServiceImpl.java)

这个 Service 层是被 `@SentinelResource` 注解修饰的，再加上 [配置文件](./src/main/resources/application.yml)
中配置了 `spring.cloud.sentinel.web-context-unify=false` ，所以监控会细分到 `Controller -> Service` 层。