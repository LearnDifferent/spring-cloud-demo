[Controller](./src/main/java/com/example/springcloud/alibaba/controller/SentinelDemoController.java) 定义两个
Endpoint，调用了同一个 [Service 方法](./src/main/java/com/example/springcloud/alibaba/service/impl/GoodServiceImpl.java)

这个 Service 中的被调用的方法添加了 `@SentinelResource` 注解，再加上 [配置文件](./src/main/resources/application.yml)
中配置了 `spring.cloud.sentinel.web-context-unify=false` ，所以监控会将这 2 个从 `Controller -> Service` 的请求，视为 2 个不同的链路。