# 微服务与微服务架构

微服务：

* 强调的是服务的大小，关注的是某个点，是具体解决某一问题，提供对应服务的一个服务应用
* 强调的是一个个的个体，每一个个体完成一个具体的任务或功能

微服务架构：

- 一种架构形模式，包含了多个微服务
- 根据业务拆分服务，彻底解耦，每个微服务提供单个业务功能的服务，一个服务做一件事情

分布式架构（分散コンピューティング）会遇到的四个核心问题及解决方案：

1. 这么多服务，客户端该如何去访问？
   - API 网关，服务路由
2. 这么多服务，服务之间如何进行通信
   - HTTP，RPC 框架，异步调用
3. 这么多服务，如何治理？
   - 服务注册与发现，高可用
4. 服务挂了，怎么办？
   - 熔断机制，服务降级 

微服务怎么拆分？（拆分的边界？）

- 功能维度
- 业务维度
- 康威定律（考虑团队结构）
- 拆分粒度

# Spring Cloud 基础概念

> 相关资料：[冒着挂科的风险也要给你们看的 Spring Cloud 入门总结](https://juejin.cn/post/6844904007975043079)或者[Spring Cloud 入门总结](https://zhuanlan.zhihu.com/p/95696180)（两个链接都是同一篇，下文会大量引用）

## Spring Cloud 简介

![](https://spring.io/img/extra/microservices-6.svg)

> 图：Spring Cloud 总体框架

Spring Cloud 简介：

* 微服务系统架构的一站式解决方案
* 构建微服务的过程中需要做如 服务发现注册 、配置中心 、消息总线 、负载均衡 、断路器 、数据监控 等操作
* Spring Cloud 提供了一套简易的编程模型，使我们能在 Spring Boot 的基础上轻松地实现微服务项目的构建

Spring Cloud 和 Spring Boot 的关系：

* Cloud 协调微服务，boot 用于快速开发微服务（jar 包）。Cloud 离不开 boot，而 boot 可以单独运行
* 也就是说，Spring Boot 专注于快速开发单个个体微服务，Spring Cloud 关注全局的微服务治理框架。

Spring Cloud 和 Dubbo 的区别：

- Spring Cloud 是微服务架构的一站式解决方案
- Dubbo 的定位是一款 RPC 框架
- Spring Cloud 抛弃了 Dubbo 的 RPC 通信，采用的是基于 HTTP 的 REST 方式
- REST 没有 RPC 通信那样好的服务调用的性能，但是更加灵活
  - 服务提供方和调用方的依赖只靠 HTTP 来连接，不存在代码级别的强依赖
  - 所以在强调快速演化的微服务环境下，REST 更为适合。

## RESTful HTTP 通信

RESTful HTTP 协议通信：

- 作用：规范了 HTTP 通信协议的标准
- HTTP Method 约束资源操作类型：GET、POST、PUT、DELETE
- REST 是面向资源的，所以 Method 规范操作类型，而路径一般使用名词
  - 比如：GET `/order/${id}` 就表示查询该 id 的 order
- 通过 HTTP 返回码来表达返回的信息，比如 200 表示成功，400 表示找不到请求路径等

---

**多个服务间通信** ，其实可以转换为 **如何调用第三方的 HTTP 服务** 。

> 调用第三方 HTTP 服务可以使用 RestTemplate、HttpClient、OkHttp 和 JDK 的 HttpUrlConnection 等。

假设现在一个 user 服务想通过 `RestTemplate` 调用 order 服务，但是 order 服务是个集群，也就是有多个 order 服务，调用哪个都可以。

也就是说，**一旦涉及到业务拆分，使用了微服务架构，就必然涉及到远程通信。而在微服务中，经常会出现服务的集群。此时集群中的服务地址就必然要维护到客户端（消费端）**。

怎么请求资源，就涉及到了 **负载均衡**。此时需要：

1. 在 user 端配置目标服务的地址列表
2. 根据地址列表，做一个负载均衡的计算，决定请求哪个服务

> 负载均衡使用 Ribbon 组件。
> 
> Ribbon 的作用（[下文中还会提到一次](#what-ribbon-does)）：
> 
> 1. 解析配置中的服务器列表
> 
> 2. 基于负载均衡的算法实现请求的分发

使用 Ribbon 可以直接利用 `@LoadBalanced` 注解。

比如在配置 `RestTemplate` 的 Bean 的时候加上 `@LoadBalanced` 注解，然后就会发送请求之前，拦截请求，并根据 `IRule` Bean 的配置来设置怎么发送请求。可以参考[下文](#use-ribbon)。

---

现在 Spring Cloud 通信一般使用的是 Open Feign。它是一个声明式的伪 RPC 客户端，可以实现面向接口编程。使用方法可以参考[下文](#open-feign)。

## CAP 定理

> 下文来自 ChatGPT，有轻微错误，仅供参考

CAP（Consistency, Availability, and Partition tolerance）用于描述分布式系统中的三个基本性能特征：一致性、可用性和分区容错性：

- 一致性（Consistency）指的是所有节点在同一时间具有相同的数据

- 可用性（Availability）指的是系统中的所有请求都可以得到响应，即使某个节点发生故障

- 分区容错性（Partition tolerance）~~指的是系统在网络分区的情况下仍然可以继续~~ （注：有问题，看下面）
  
  - 网络分区：当网络中的一部分节点无法与另一部分节点进行通信时，网络被分割成不同的部分

CAP 定理指出，<u>在分布式系统中，只能同时保证两个，而不能三个同时保证</u>。

> 例如，一个典型的分布式系统可能会使用一致性和可用性的组合，以确保所有的节点在同一时间具有相同的数据，并且在任何情况下都可以提供响应。但是，这种组合不能保证系统在网络分区的情况下仍然可以继续工作。

---

最终一致性：

- 系统中所有节点最终都会保持相同的状态，即使在网络分区的情况下也是如此

- 优点：可以在网络分区的情况下继续工作

- 缺点：可能需要更长的时间来达到一致性

强一致性：

- 所有节点在同一时间具有相同的数据

- 优点：可以在更短的时间内达到一致性

- 缺点：不能在网络分区的情况下继续工作

> 例如，在银行的账户系统中，最终一性可能会导致客户在某个时间点看到不同的余额，但是最终会达到一致，而强一致性可以确保客户在任何时间点都看到相同的余额。

如果你需要在网络分区的情况下继续工作，那么最终一致性可能是最好的选择。

如果你需要在更短的时间内达到一致性，那么强一致性可能是最好的选择。

---

> 以下是之前整理的笔记

CAP 原则又称 CAP 定理，指的是在一个分布式系统中，**一致性（Consistency）**、**可用性（Availability）**、**分区容错性（Partition tolerance）**，这三个要素<u>最多只能同时实现两点</u>，不可能三者兼顾。

一致性（Consistency）：这里指的是强一致性

可用性（Availability）：请求会得到响应，而不是持续的等待

Partition tolerance（分区容错性）：

- 网络分区（脑裂）：当网络发生异常，导致分布式系统中部分节点之间的网络延时不断增加，导致组成分布式系统的所有节点，只有部分节点之间能够进行正常通信，而另一些节点则不能
- 分区容错性约束了一个分布式系统需要具有的如下特性：分布式系统在遇到任何网络分区故障的时候，仍然需要能够保证对外提供满足一致性和可用性的服务，除非是整个网络环境都发生了故障

简单来说，就是 **Partition tolerance（分区容错性）就是保证服务因为网络而故障的时候，业务端还能正常运行** 。实现起来，就是 **集群节点** 和 **跨区域的高可用** 。

在分布式系统中，Partition tolerance 必须得到满足，所以只能在 Consistency 和 Availability 之间权衡。

> 延伸阅读：[分布式的特性、面临的问题、中心化 & 去中心化、CAP理论、BASE理论](https://blog.csdn.net/qwesxd/article/details/108589781)

例如：

- Eureka 保证 AP：
  - <u>Eureka 各个节点是相等的</u>，如果发现节点连接失败，就自动转换到其他节点，也就是说，只要还有一台 Eureka，就能确保服务的可用性（保证 Availability）
  - 只不过，无法保证 Eureka 返回的信息是最新的（牺牲了 Consistency）
- Zookeeper 保证 CP：
  - 如果主要的 Server（Master）出现问题，就要重新选举，选举可能会耗费时间，导致服务暂时不可用（牺牲 Availability）
  - 但是这样可以保证所有数据备份，在同一时刻的值是相等的（要不无法读取，要不就读取最新的，保证 Consistency）

可以参考 [下文的 Eureka 部分](#eureka-ap)。

---

> 下文节选自：[最终一致性的实现方案 - Vincent-yuan](https://www.cnblogs.com/Vincent-yuan/p/16074577.html) （这篇文章推荐阅读）

数据一致性的基础理论：

强一致：

- 含义：当更新操作完成之后，任何多个后续进程或者线程的访问都会返回最新的更新过的值。

- 这种是对用户最友好的，就是用户上一次写什么，下一次就保证能读到什么。

- 根据 CAP 理论，这种实现需要牺牲可用性。

弱一致性：

- 含义：系统并不保证续进程或者线程的访问都会返回最新的更新过的值。

- 系统在数据写入成功之后，不承诺立即可以读到最新写入的值，也不会具体的承诺多久之后可以读到。

最终一致性：

- 含义：弱一致性的特定形式。系统保证在没有后续更新的前提下，系统最终返回上一次更新操作的值。

- 在没有故障发生的前提下，不一致窗口的时间主要受通信延迟，系统负载和复制副本的个数影响。DNS 是一个典型的最终一致性系统。

---

CAP定理（ていり）：

- ノード間のデータ複製において、同時に次の3つの保証を提供することはできない
- 一貫性 (Consistency)
  - すべてのデータ読み込みにおいて、最新の書き込みデータもしくはエラーのどちらかを受け取る
  - Every read receives the most recent write or an error
- 可用性 (Availability)
  - ノード障害により生存ノードの機能性は損なわれない。つまり、ダウンしていないノードが常に応答を返す。単一障害点が存在しないことが必要
  - Every request receives a (non-error) response, without the guarantee that it contains the most recent write
- 分断耐性 (Partition-tolerance)
  - システムは任意の通信障害などによるメッセージ損失に対し、継続して動作を行う。通信可能なサーバーが複数のグループに分断されるケース（ネットワーク分断）を指し、1つのハブに全てのサーバーがつながっている場合は、これは発生しない。ただし、そのようなネットワーク設計は単一障害点をもつことになり、可用性が成立しない。RDBではそもそもデータベースを分割しないので、このような障害とは無縁である。
  - The system continues to operate despite an arbitrary number of messages being dropped (or delayed) by the network between nodes

## 服务管理

### 服务注册基础概念

上面提到微服务间的通信。如果在通信时，维护服务地址都在客户端（消费者）完成的话，会出现问题：

1. 如果服务宕机（下线），要怎么动态感知？服务就算下线了，客户端也可能向其发出请求。这个请求肯定是无法响应的。
2. 客户端（消费者）维护地址会非常困难。

解决上面的问题，只需要 **服务注册中心** 就可以了。

---

在服务注册中心里面，只需要存储 key-value 结构的数据就可以了。

比如某个服务叫 user-provider，它有多个地址可以提供相同的服务（也就是集群），那么 key 就是 user-provider，value 就是可以提供服务的所有地址。

消费者（客户端）只需向服务注册中心说“我需要 user-provider“，服务注册中心就会返回集群中的所有地址。然后消费者通过负载均衡去确定请求集群中的某个地址。

---

服务注册中心还会进行心跳（heartbeat）检测，去检测服务提供者是否存活。

除此之外，还可以进行节点的扩容，也就是获取新的服务提供者。

### 常见的服务注册中心解决方案

各个方案之间的不同，基本上围绕这几个方面：

- 推送方式：消费者和服务注册中心之间的通信采用的是 push、pull 还是 long-poll 方式？
- 存储：是否持久化
- 高可用机制：集群特性，即，是否有选举特性、是否保证在集群中的每个节点都要一致（一致性问题）
- CAP 特性：CP or AP
- API 提供形式：HTTP 协议、Netty（Socket）通信

Eureka：

- 非持久化存储，存在内存中
- AP 模型，优先保证高可用
- 集群中每个节点的角色是相等的，不需要保证一致性，只要保证高可用

# Spring Cloud 项目基本说明

总体说明：

* 创建方式：首先创建 Maven 总工程，然后再创建多个 Module 来演示微服务
* 比如关于 api 的 Module 就只负责 1 个功能
* 项目命名类似 [springcloud-provider-dept-8001](./springcloud-provider-dept-8001) ，代表是 dept 的 provider，端口号为 8001
* 每一个 Module 中的 Spring Boot 项目都需要创建一个 controller，来让其他的服务访问

Spring Cloud 架构的使用步骤：

1. 导入依赖
2. 编写配置文件
3. 使用 @EnableXxxx 注解开启功能
4. 配置类

---

演示项目使用的依赖版本：

* SpringCloud：Hoxton.SR8
* SpringBoot：2.3.3.RELEASE
* 以下版本全为：2.2.5.RELEASE
  * Eureka Server：spring-cloud-starter-netflix-eureka-server
  * Provider：spring-cloud-starter-netflix-eureka-client
  * Consumer 和 Ribbon：
    * spring-cloud-starter-netflix-eureka-client
    * spring-cloud-starter-netflix-ribbon
  * Open Feign：spring-cloud-starter-openfeign
  * Hystrix：
    * spring-cloud-starter-netflix-hystrix
    * spring-cloud-starter-netflix-hystrix-dashboard
  * Zuul：spring-cloud-starter-netflix-zuul
  * Config：
    * spring-cloud-config-server
    * spring-cloud-starter-config

---

如果需要在多个数据库中，创建相同数据的表（数据相同，但所属数据库不同），那么在每个数据库内，可以先创建含有 `db_source` 字段的表，`db_source` 字段用于区分数据库的来源：

```sql
CREATE TABLE `dept` (
  `deptno` bigint(20) NOT NULL AUTO_INCREMENT,
  `dname` varchar(60) DEFAULT NULL,
  `db_source` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`deptno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

创建好后，插入的时候使用 `DATABASE()` 来自动生成数据库名称：

```sql
INSERT INTO `dept` (`deptno`, `dname`, `db_source`)
VALUES
        (1,'开发部',DATABASE()),
        (2,'人事部',DATABASE()),
        (3,'财务部',DATABASE()),
        (4,'市场部',DATABASE()),
        (5,'运维部',DATABASE()),
        (6,'管理部',DATABASE()),
        (7,'后勤部',DATABASE());
```

完成后，Java 实体类可以参考：[springcloud-api](./springcloud-api) 的 [Dept.java](./springcloud-api/src/main/java/com/example/springcloud/api/pojo/Dept.java)

> 注意：因为 [Dept.java](./springcloud-api/src/main/java/com/example/springcloud/api/pojo/Dept.java) 是在单独的 Module（也就是 [springcloud-api](./springcloud-api) ）中的，所以如果需要使用这个实体类的时候，需要先引入 [springcloud-api](./springcloud-api) 的依赖。参考：[springcloud-consumer-dept-80 中的 pom.xml](./springcloud-consumer-dept-80/pom.xml)

# 微服务中的 Provider 和 Consumer

## Provider 和 Consumer 的基本介绍及项目中的使用

微服务中的 Provider，相当于房东。当微服务中的 Consumer 想要使用资源的时候，需要调用 Provider 提供的服务，相当于租客。

项目中的 Consumer：

- [springcloud-consumer-dept-80](./springcloud-consumer-dept-80)
- [springcloud-consumer-dept-openfeign](./springcloud-consumer-dept-openfeign)
- [springcloud-consumer-hystrix-dashboard-9001](./springcloud-consumer-hystrix-dashboard-9001)

项目中的 Provider：

- [springcloud-provider-dept-8001](./springcloud-provider-dept-8001)
- [springcloud-provider-dept-8002](./springcloud-provider-dept-8002)
- [springcloud-provider-dept-8003](./springcloud-provider-dept-8003)
- [springcloud-provider-dept-hystrix-8001](./springcloud-provider-dept-hystrix-8001)
- [springcloud-provider-dept-hystrix-8002](./springcloud-provider-dept-hystrix-8002)

## Provider 和 Consumer 与 Eureka

房东 / Provider 和租客 / Consumer 之间需要联系，房东可以通过街边小广告的方式贴出房源信息，但是这样会出现问题：

1. 计算机资源消耗：不是租客也会看到出租广告，广告的效果太差，消耗大量资源却效果差
2. Consumer 依旧麻烦：租客找房依旧麻烦，还要专门去街边收集广告

所以需要提供统一房源的「中介」，Provider 和 Consumer 都去通过中介来交流：

* Consumer(s) 去中介那里获取信息列表
* Provider(s) 去中介那里注册

然而这是还是有问题：

1. 房东注册之后如果不想卖房子了怎么办？
   1. 我们是不是需要让房东<u>定期续约</u>？
   2. 如果房东不进行续约是不是要将他们从中介那里的注册列表中<u>移除</u>？
2. <u>租客</u>是不是也要进行<u>注册</u>呢？
3. 中介可不可以做<u>连锁店</u>呢？如果这一个店因为某些不可抗力因素而无法使用，那么我们是否可以换一个连锁店呢？

所以，解决方案是：

1. Consumer(s) 注册并获取资源信息列表
2. Provider(s) 注册并定期续约，否则被移除
3. 提供多个「中介」（也就是 Replication），让中介连接 Consumer(s) 和 Provider(s)

# Eureka：服务注册与发现

## Eureka 入门

What is Service discovery?

* Service Discovery is the process of how microservices discover each other over a network. There are two main components of it in terms of Eureka service:
  * Eureka server (service registry): It is a server that stores the addresses (host and ports) of all the registered microservices.
  * Eureka Client: Its a microservice registered on the central server and it updates and retrieves addresses to/from the central Eureka server.
* Eureka provides service discovery in a microservices architecture. This involves two steps on a high level:
  * Services registers themselves on the Eureka server and details like name, host, and port are stored there.
  * Details of other registered microservices become available for the registered service.

Eureka 是一个服务发现（Service registry）框架。

> Eureka是基于REST（代表性状态转移）的服务，主要在AWS云中用于定位服务，以实现负载均衡和中间层服务器的故障转移。我们称此服务为Eureka服务器。Eureka还带有一个基于Java的客户端组件Eureka Client，它使与服务的交互变得更加容易。客户端还具有一个内置的负载平衡器，可以执行基本的循环负载平衡。在Netflix，更复杂的负载均衡器将Eureka包装起来，以基于流量，资源使用，错误条件等多种因素提供加权负载均衡，以提供出色的弹性。

---

服务发现模式：

> 服务发现：整个过程有三个角色，分别为<u>服务提供者</u>、<u>服务消费者</u>和<u>服务中介</u>

* 服务提供者（Service Provider）：向外界提供能够执行的服务
* 服务消费者（Service Consumer）：使用服务的用户
* 服务中介：
  * 服务提供者和服务消费者之间的“桥梁”
  * 服务提供者和消费者都可以在中介那里注册
  * 服务提供者需要定期续约，否则会被移除
  * 服务消费者需要使用服务的时候会去找中介

---

服务注册（Register）：

* Eureka Client 向 Eureka Server 注册时，需要提供 Eureka Client 的 Meta Data（元数据）
* Meta Data 有 IP、端口、URL、运行状况等

服务续约（Renew）：

* Eureka Client 默认会每隔 30 秒发送一次心跳（Heartbeat）来 renew（续约）
* Renew 的目的：告知 Eureka Server 该客户端，Eureka Client 仍然存在且没有问题

服务剔除（Eviction）：

* 如果 Eureka Client 超过 3 个续约周期（也就是默认超过 90 秒）都没有 Renew（也就是没有发送服务续约/心跳），该 Eureka Client 实例将会从 Eureka Server 的注册表中剔除（Eviction）

获取注册列表信息（Fetch Registries）：

* Eureka Client 会从 Eureka Server 那里 fetch registries（获取注册表信息），并将注册表信息缓存在本地
* Eureka Client 会使用注册表信息查找其他服务，从而进行远程调用
  * 注册表信息默认每 30 秒更新一次
  * 如果 Eureka Client 收到更新的信息与之前缓存中的不符，客户端会自动处理
  * 如果注册列表信息无法及时匹配，Eureka Client 会重新获取整个注册表信息
* Eureka Server 会存储注册列表信息
  * Eureka Server 对整个注册表以及每个应用程序的信息进行了压缩
  * 压缩内容和没有压缩的内容完全相同
* Eureka Client 和 Eureka Server 可以使用 JSON / XML 格式进行通讯
* 默认的情况下 Eureka Server 使用压缩 JSON 格式来获取注册列表的信息

服务下线（Cancel）：

1. Eureka Client 在服务下线的时候，会发送下线请求（Cancel）给 Eureka Server
2. Eureka Server 接到请求后，会将该 Eureka Client 实例信息，从 Eureka Server 的实例注册表中删除
3. 下线的请求不会自动完成，需要调用 `DiscoveryManager.getInstance().shutdownComponent();`

![](https://static001.infoq.cn/resource/image/8b/8b/8bf6e27c60dbfd717b6830263890368b.png)

> 图：Eureka 架构图（来自：[微服务注册中心 Eureka 架构深入解读](https://www.infoq.cn/article/jldjq*3wtn2pcqtdyokh)）

除了 Eureka，服务发现的组件还有：

* Zookeeper
* Consul

## Eureka 相关进阶概念

> Netflix 在设计的时候，<span id='eureka-ap'>遵循了 AP 原则</span>。
> 
> **Eureka 可以应对网络故障导致部分节点失联的情况**，而不会像 Zookeeper 那样整个注册服务瘫痪。

***

Eureka 的自我保护机制（Self Preservation）：

* 作用：如果某一时刻某个微服务无法使用，Eureka 不会立刻注销任何微服务，而会保存注册表中的微服务的数据，当网络恢复后，会自动退出自我保护机制
* 为什么使用：
  * 有些时候只是网络故障导致微服务与 Eureka 之间无法通信，而微服务本身没有问题，此时不应该在 Eureka Server 中注销该服务，所以有了自我保护机制
  * 也就是说，<u>在网络抖动或网络不稳定的情况下，避免误删除</u>

> 默认在 15 分钟内超过 85% 的节点都心跳失败（没有正常的心跳），Eureka 就会认为客户端与注册中心出现了网络故障，此时有以下情况：
> 
> 1. Eureka 不再从注册中心剔除其注册列表中的实例（即使过了 90秒 也不会）
> 2. Eureka 仍然能够接受新服务的注册和查询请求，但是不会同步到其他节点上，只保证当前节点的可用性
> 3. 当网络恢复稳定，当前实例新的注册信息再同步到其他节点中

---

可以登陆 `Eureka网址/eureka/apps` 路径，查看注册在 Eureka 上的信息。



更多关于 Eureka 的知识（初始注册策略等），可以查看文章：[深入浅出 Spring Cloud 之 Eureka](https://juejin.cn/post/6844904001444511758)

## Eureka Server 和 Client 在项目中的使用

注册中心集群：生产环境可能需要多个 Eureka Server 组成集群，比如 7001、7002 和 7003 端口的 Server 组成集群：

* 集群可以在出问题后，自动转换 Server
* 在演示项目中，需要端口号为 7001 的 Server 绑定 7002 和 7003；7002 和 7003 也要绑定其他的 Server

为了模拟多台机器，需要 `sudo vim /etc/hosts`，然后添加 host 映射：

```
127.0.0.1 eureka7001.com
127.0.0.1 eureka7002.com
127.0.0.1 eureka7003.com
```

Eureka 相关代码：

- [springcloud-eureka-7001](./springcloud-eureka-7001)
- [springcloud-eureka-7002](./springcloud-eureka-7002)
- [springcloud-eureka-7003](./springcloud-eureka-7003)

拓展阅读：

* [Service Discovery: Eureka Clients](https://cloud.spring.io/spring-cloud-netflix/multi/multi__service_discovery_eureka_clients.html) 
* [Eureka集群部署以及踩坑记录](https://my.oschina.net/icebergxty/blog/3080748)

# Open Feign：HTTP Client

> Eureka 框架中的 注册、续约 等，底层都是使用的 RestTemplate

## 了解 RestTemplate 基础概念

在学习 [Open Feign](#open-feign) 之前需要了解 RestTemplate。

> 本项目中，RestTemplate 和 Ribbon 相关代码：[springcloud-consumer-dept-80](./springcloud-consumer-dept-80)
> 
> 我在 [LearnDifferent/github-stars](https://github.com/LearnDifferent/github-stars) 中，也使用过 RestTemplate，可以查看 [RestTemplate 的配置类](https://github.com/LearnDifferent/github-stars/blob/master/src/main/java/com/github/learndifferent/githubstars/config/RestTemplateConfig.java) 和 [RestTemplate 在 Service 中的使用](https://github.com/LearnDifferent/github-stars/blob/master/src/main/java/com/github/learndifferent/githubstars/service/impl/RepoServiceImpl.java)

RestTemplate 是 Spring 提供的一个访问 Http 服务的客户端类：

* 微服务之间需要使用 RestTemplate 来完成调用
* 相当于一个入口，用户通过这个入口来发出请求，这个入口接到请求后，交给各个服务来处理请求
* 其实 **就是在微服务间，发送请求和响应请求**

<span id="use-ribbon">需要先在配置类中添加 RestTemplate 的 Bean，并加上 `@LoadBalanced` 负载均衡的注解，来实现（Ribbon）负载均衡的服务调用</span>，参考：

- [ConfigBean.java](./springcloud-consumer-dept-80/src/main/java/com/example/springcloudconsumerdept80/config/ConfigBean.java) 的 `public RestTemplate getRestTemplate()`

在 Consumer 中使用的时候，可以参考：

- [ConsumerController.java](./springcloud-consumer-dept-80/src/main/java/com/example/springcloudconsumerdept80/controller/ConsumerController.java)

再比如，这个时候「消费者 B」需要调用「提供者 A」所提供的服务时，需要这么写：

```java
@Autowired
private RestTemplate restTemplate;
// 这里是提供者 A 的 ip 地址，但是如果使用了 Eureka 那么就应该是提供者 A 的 application name
private static final String SERVICE_PROVIDER_A = "http://localhost:8081";

@PostMapping("/judge")
public boolean judge(@RequestBody Request request) {
    String url = SERVICE_PROVIDER_A + "/service1";
    return restTemplate.postForObject(url, request, Boolean.class);
}
```

## <span id="open-feign">Open Feign 快速入门</span>

> Open Feign is a Java to *HTTP client* binder

> Open Feign 是声明式的 web service 客户端，对 RestTemplate 和 Ribbon 做了进一步的封装，可以实现类似于 RPC 的面向接口开发

使用 RestTemplate 不够方便，每次都要调用 RestRemplate 的 API，而使用 Open Feign 的话，只需要创建接口（接口上使用注解来配置），然后在其他地方注入调用就可以了（类似于 DAO 接口）。

> OpenFeign 是运行在消费者端的。使用了 Ribbon 进行负载均衡，所以 OpenFeign 直接内置了 Ribbon

> Open Feign 也内置了 Hystrix

在使用了 Open Feign 之后我们就可以这样编写接口（相当于 Service 接口）：

```java
// 使用 @FeignClient 注解来指定提供者的名字
@FeignClient(value = "eureka-client-provider")
public interface TestClient {

    // 这里一定要注意需要使用的是提供者那端的请求相对路径
    // 相当于映射被调用的服务代码
    @RequestMapping(value = "/provider/xxx", method = RequestMethod.POST)
    CommonResponse<List<Plan>> getPlans(@RequestBody planGetRequest request);
}
```

在 Controller 就可以像调用 Service 层代码一样调用它：

```java
@RestController
public class TestController {
    @Autowired
    private TestClient testClient;

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public CommonResponse<List<Plan>> get(@RequestBody planGetRequest request) {
        return testClient.getPlans(request);
    }
}
```

## Open Feign 优化配置

Open Feign 默认使用的是性能没那么好的 HttpClient，可以在 [application.yml](./springcloud-consumer-dept-openfeign/src/main/resources/application.yml) 中关闭 HttpClient，并替换为 OK HTTP Client：

```yaml
feign:
  # 关闭默认的 HttpClient
  httpclient:
    enabled: false
  # 开启 Ok HTTP Client
  okhttp:
    enabled: true
```

## Open Feign 在项目中的使用

Open Feign 相关代码：

- 总体模块：[springcloud-consumer-dept-openfeign](./springcloud-consumer-dept-openfeign)
- Open Feign Client 接口的写法：[DeptClient.java](./springcloud-consumer-dept-openfeign/src/main/java/com/example/springcloudconsumerdept80/service/DeptClient.java)
- 在 Controller 中使用 Open Feign： [ConsumerController.java](./springcloud-consumer-dept-openfeign/src/main/java/com/example/springcloudconsumerdept80/controller/ConsumerController.java)
- 主启动类：[ConsumerDeptFeign80.java](./springcloud-consumer-dept-openfeign/src/main/java/com/example/springcloudconsumerdept80/ConsumerDeptFeign80.java)

# Ribbon：负载均衡

## Ribbon 相关基础概念

Ribbon 是 Netflix 公司的一个开源的 *负载均衡（Load Balance）* 项目，**是一个客户端/进程内负载均衡器，<u>运行在消费者端（集成在 Consumer 中）</u>**

Load balancing（负载均衡 / LB）：

* Load balancing refers to the process of distributing a set of tasks over a set of resources (computing units), with the aim of making their overall processing more efficient.
* Load balancing techniques can optimize the response time for each task, avoiding unevenly overloading compute nodes while other compute nodes are left idle.
* Load balancing is the subject of research in the field of parallel computers. Two main approaches exist:
  * static algorithms, which do not take into account the state of the different machines
  * dynamic algorithms, which are usually more general and more efficient, but require exchanges of information between the different computing units, at the risk of a loss of efficiency.

サーバロードバランシング（Server Load Balancing）：

* クライアントとサーバの間にロードバランサ（負荷分散装置）を設置し、複数のサーバが分散処理を行う
* 利用者の多いWebアプリケーションやネットワークゲームの運営などに適しており、サーバ１台では処理しきれない場合に、この技法を利用することで効率よくサーバの数を増やすことができる
* また、故障や保守によりサーバが停止した際にも、サービスを続行させることができる

集中式与进程内负载均衡的区别：

* 集中式负载均衡：
  * 在 consumer 和 provider 之间使用独立的负载均衡设施（可以是硬件，如 F5；也可以是软件，如 nginx）
  * 由该设施负责把「访问请求」通过某种策略转发至 provider
* 进程内负载均衡/进程内 LB：
  * 将负载均衡逻辑集成到 consumer，consumer 从服务注册中心获知有哪些地址可用，然后自己再从这些地址中选择出一个合适的服务器（provider）
* Ribbon 就属于后者，它只是一个类库，集成于 consumer 进程，consumer 通过它来获取到 provider 的地址。

拓展阅读：[很全！浅谈几种常用负载均衡架构](https://cloud.tencent.com/developer/article/1437969)

## 为什么需要 Ribbon？

### Ribbon 的使用场景

> 负载均衡（LB）的作用：将用户的请求平摊分配到多个服务上，达成系统的 HA（高可用）

假设需要设计一个秒杀系统，为了整个系统的<u>高可用</u>，需要做一个秒杀系统的集群。在设置了集群后，Consumer 就可以有多个调用途径了：

* Consumer <---注册并获取服务列表---> 中介
* Consumer ---根据服务列表，选择---> 秒杀系统1（或者秒杀系统2，或者秒杀系统3）

但是，这样设计有个问题：如果没有进行<u>均衡操作</u>，可能会出现大量对秒杀系统 1 的调用请求，而秒杀系统 2 和 3 基本没有请求。这就会导致秒杀系统 1 崩溃，而另外 2 个秒杀系统闲置。

所以，需要<u>消费者端的负载均衡器</u> Ribbon：

* Consumer <---注册并获取服务列表---> 中介
* Consumer ---获取服务列表后，通过 Ribbon 内部的负载均衡算法，均衡调用：---> 秒杀系统 1 /秒杀系统 2/秒杀系统 3

<span id="what-ribbon-does">Ribbon 的作用</span>：

1. 解析配置中的服务器列表
2. 基于负载均衡的算法实现请求的分发

### Ribbon 对比 Nginx

Nginx（反向代理服务器）和 Ribbon 的对比：

* <u>Nginx 是集中式的负载均衡器</u>，它会将所有请求都集中起来，然后再进行负载均衡
  * 在 Nginx 中，请求是先进入独立的负载均衡器，再负载均衡调度多个系统
* Ribbon 是先在（消费者）客户端进行负载均衡，再发送请求到各个（服务提供者）系统

## Ribbon 的几种负载均衡算法及项目中的配置

Ribbon 中有多种负载均衡调度算法，其默认是使用的 RoundRobinRule（轮询策略）：

* RoundRobinRule（轮询策略）：以线性轮询方式获取服务，如果第一次轮询没有找到可用的 provider，就继续轮寻；如果轮询超过了 10 轮还没有找到，就返回 null
* RandomRule（随机策略）：从所有可用的 provider 中随机选择一个。
* RetryRule（重试策略）：先按照 RoundRobinRule 策略获取 provider，若获取失败，则在指定的时限（默认为 500 毫秒）内重试

如果要使用其他负载均衡算法，可以在配置文件中修改，或者在 Java 的配置类中覆盖默认规则：

1. 配置文件：

```yml
providerName:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
```

2. `@Configuration` 配置类（可以查看 [springcloud-consumer-dept-80](./springcloud-consumer-dept-80) 模块的 [ConfigBean.java](./springcloud-consumer-dept-80/src/main/java/com/example/springcloudconsumerdept80/config/ConfigBean.java) ）：

```java
// 默认是轮询，可以通过加入 Spring 容器来改变算法策略
@Bean
public IRule rule() {
    // 这里改成随机
    return new RandomRule();
}
```

在 Ribbon 中还可以自定义负载均衡算法，需要实现 IRule 接口，然后修改配置文件或者，自定义 Java Config 类（Java Config 类要单独一个目录和类），然后把 `@RibbonClient(name = "自定义名称", configuration = 该自定义算法所在的类.class)` 注解放在启动类上，可以 [参考这里](https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-ribbon.html)。

# Hystrix：熔断和降级

## Hystrix 相关基础

Hystrix 就是一个能进行 **熔断（meltdown）** 和 **降级（downgrade）** 的库，通过使用它能提高整个系统的弹性：

* 在分布式环境中，每个服务都可能有相应的依赖性质的服务（服务依赖项），如果服务 A 需要依赖另一个服务 B，但是 B 出错了，就会产生问题
* Hystrix 是一个库，通过<u>添加等待时间容限和容错逻辑</u>来帮助控制分布式服务之间的交互
* Hystrix 能够<u>隔离服务之间的访问点</u>，<u>停止服务之间的级联故障</u>并<u>提供后备选项</u>

> マイクロサービスでAPI通信しているときに、一部で通信エラーが発生した場合に
> アクセスを遮断して切り離す必要があります
> その際に用いられるのがサーキットブレイカー（Circuit Breaker/熔断器）です
> 
> In a distributed environment, inevitably some of the many service dependencies will fail. Hystrix is a library that helps you control the interactions between these distributed services by adding latency tolerance and fault tolerance logic. Hystrix does this by isolating points of access between the services, stopping cascading failures across them, and providing fallback options, all of which improve your system’s overall resiliency.
> 
> Hystrix是一个用于处理分布式系统的延迟和容错的开源库。在分布式系统里，许多依赖不可避免的会调用失败，比如请求超时，处理异常等等，Hystirx能够保证在一个依赖问题的情况下，不会导致整体服务失败，避免级联故障。以提高分布式系统的弹性。

> Netflix Hystrixはフォールトトレランス(障害が起きてもサービスを継続する)のためのライブラリで、サーキットブレイカーの実装が含まれています
> 
> Hystrix is a latency and fault tolerance library designed to isolate points of access to remote systems, services and 3rd party libraries, stop cascading failure and enable resilience in complex distributed systems where failure is inevitable.
> 
> Hystrix 又被称为“断路器/熔断器” ，他本身就是一种开关装置，当某个服务单元发生故障之后，通过断路器的故障监控（类似于熔断保险丝），向调用方法返回一个服务预期的，可处理的备选响应（FallBack），而不是长时间的等待或者抛出调用方法无法处理的异常，这样就可以保证了服务调用方的线程不会被长时间的占用，从而避免了故障在分布式系统的蔓延，甚至雪崩

Hystrix is designed to do the following:

* Give protection from and control over latency and failure from dependencies accessed (typically over the network) via third-party client libraries.
* Stop cascading failures in a complex distributed system.
* Fail fast and rapidly recover.
* Fallback and gracefully degrade when possible.
* Enable near real-time monitoring, alerting, and operational control.

[Hystrix 使用手册 | 官方文档翻译](https://www.cnblogs.com/flashsun/p/12579367.html)

## Hystrix 重点概念

**熔断和降级**：

- 服务熔断是在 Provider 处理
- 服务降级是在 Consumer 处理
- 都可以理解为备用方案

服务雪崩：

* 【服务 A】--调用-->【服务 B】--调用-->【服务 C】
* 如果服务 C 因为某些原因出错，就会有大量请求阻塞在 C 这里
* 此时 C 无法返回响应给 B，那 B 也会阻塞，导致 A 也阻塞崩溃
  * 阻塞崩溃：请求会消耗占用系统的线程、IO 等资源，消耗完了，系统服务器就会崩溃
* 这种服务器接连崩溃的情况，就是服务雪崩

熔断：

* 熔断用于解决服务雪崩，类似于“跳闸”
* 当指定时间窗内的请求失败率达到设定阈值时，系统将通过 *断路器* 直接将此请求链路断开
* Hystrix 中的<u>断路器模式</u>就属于熔断

使用简单的 @HystrixCommand 注解来标注某个方法，这样 Hystrix 就会使用 *断路器* 来“包装”这个方法，每当调用时间超过指定时间时(默认为1000ms)，断路器将会中断对这个方法的调用。

也可以对这个注解的很多属性进行设置，比如设置超时时间，像这样：

```java
@HystrixCommand(commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1200")})
public List<Xxx> getXxxx() {
    // ...省略代码逻辑
}
```

降级：

* 为了更好的用户体验，当一个方法调用异常时，通过执行另一种代码逻辑来给用户友好的回复
* Hystrix 的 *后备处理模式* 就属于降级

`@HystrixCommand` 注解的 `fallbackMethod` 属性可以给一个方法设置备用的代码逻辑。

假设出现了热点新闻，我们会推荐给用户查看详情，然后用户会通过 id 去查询新闻的详情，但是因为这条新闻太火了，大量用户同时访问可能会导致系统崩溃，那么我们就进行 *服务降级*，一些请求会做一些降级处理比如当前人数太多请稍后查看等等：

```java
@HystrixCommand(fallbackMethod = "getHystrixNews")
@GetMapping("/get/news")
public News getNews(@PathVariable("id") int id) {
    // 调用新闻系统的获取新闻 api 代码逻辑省略
}

public News getHystrixNews(@PathVariable("id") int id) {
    // 做服务降级
    // 返回当前人数太多，请稍后查看
}
```

---

舱壁模式：

* 在不使用舱壁模式的情况下，服务 A 调用服务 B，这种调用默认的是使用同一批线程来执行的
  * 在一个服务出现性能问题的时候，就会出现所有线程被刷爆并等待处理工作，同时阻塞新请求，最终导致程序崩溃
* 而舱壁模式会将远程资源调用隔离在他们自己的线程池中，以便可以控制单个表现不佳的服务，而不会使该程序崩溃

Hystrix 仪表盘：

* 用来实时监控 Hystrix 的各项指标信息

## Hystrix 的使用

项目中的相关代码：

- Provider 端：
  - [springcloud-provider-dept-hystrix-8001](./springcloud-provider-dept-hystrix-8001)
  - [springcloud-provider-dept-hystrix-8002](./springcloud-provider-dept-hystrix-8002)
- Consumer 端：
  - 内置 Hystrix 的 Feign：[springcloud-consumer-dept-openfeign](./springcloud-consumer-dept-openfeign)
  - Hystrix Dashboard：[springcloud-consumer-hystrix-dashboard-9001](./springcloud-consumer-hystrix-dashboard-9001)

参考资料：[Spring BootでCircuit Breaker(Spring Cloud Netflix Hystrix)を試す](http://pppurple.hatenablog.com/entry/2017/01/11/235814)

Circuit Breaker：

フォールバックされることが確認できたので、
APIコールが遮断されること(サーキットがオープンになること)を確認してみます。

@HystrixCommandのcommandPropertiesで各設定が出来るので、設定値について確認してみます。

`circuitBreaker.enabled`

* サーキットブレイカーを有効にするかどうかの設定。デフォルトは有効。

`circuitBreaker.requestVolumeThreshold`

* サーキットがオープンになるエラー回数。デフォルトは20。
* Hystrixではローリングウィンドウで状態を管理しており、そのローリングウィンドウ内でのエラー回数

```java
@HystrixCommand(
        fallbackMethod = "fallbackText",
        commandProperties = {
                @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
                @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5")
        }
)
```

`circuitBreaker.sleepWindowInMilliseconds`

* サーキットがオープンした後、sleepWindowInMilliseconds待って再度リクエストを受け付けます。デフォルトは5,000msec。
* 再度受け付けたリクエストが失敗した場合オープンの状態のままとなり、リクエストが成功した場合はサーキットがクローズします。

5000msecを指定して確認してみます：

```java
@HystrixCommand(
        fallbackMethod = "fallbackText",
        commandProperties = {
                @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
        }
)
```

`circuitBreaker.errorThresholdPercentage`

* サーキットがオープンになるエラーパーセンテージ。デフォルトは50％。

Hystrix Dashboardで50％以上になった時にサーキットがオープンになるのを見てみます：

```java
@HystrixCommand(
        fallbackMethod = "fallbackText",
        commandProperties = {
                @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")
        }
)
```

---

Hystrix Dashboard：

Hystrix DashboardはリアルタイムでHystrixメトリックを監視できるダッシュボードです。

Hystrix Dashboardを利用するには、spring-cloud-starter-netflix-hystrix-dashboardをdependencyに追加して、spring-boot-starter-actuatorを有効にします。

SpringBootのmainクラスに`@EnableHystrixDashboard`をつけます。

あとは、アプリケーションを起動して、http://localhost:9001/hystrix にアクセスします。（9001 是 Dashboard 消费者的端口，记得还要加上 /hystrix）

Hystrix Clientのアプリケーションの場合、http://hystrix-app:port/hystrix.stream がエンドポイントになっているので、http://localhost:8001/hystrix.stream を指定して「Monitor Stream」をクリックします。（8001 是 Provider 的端口，访问的时候要加上 /hystrix.stream）

Dashboard 图形信息的含义：

![](https://img2018.cnblogs.com/blog/1107037/201909/1107037-20190908231005040-85750864.png)

![](https://img2018.cnblogs.com/blog/1107037/201909/1107037-20190908231125067-850469310.png)

参考资料：[hystrixDashboard(服务监控)](https://www.cnblogs.com/yufeng218/p/11489175.html) 或者 [这篇](https://blog.csdn.net/VisionLau/article/details/110803184)

# Zuul：微服务网关

## Zuul 基础

API Gateway（网关）：

* 系统唯一对外的入口，介于客户端与服务器端之间
* 用于对请求进行鉴权、限流、 路由、监控等功能

---

Eureka Server 是 Provider（服务提供者）的统一入口。

在整个应用中，除了 Provider 还存在很多 Consumer，Consumer 也要向 Eureka Server 进行注册，不过这些 Consumer(s) 并不受 Eureka Server 的管理。

用户需要调用消费者工程（Consumer）的话，也需要一个消费者（Consumer）的统一入口。

因为，虽然用户也能直接访问消费者工程（Consumer），但是这样不便于访问和管理，所以才需要消费者的统一入口。

> ZUUL 是从设备和 web 站点到 Netflix 流应用后端的所有请求的前门。作为边界服务应用，ZUUL 是为了实现动态路由、监视、弹性和安全性而构建的。它还具有根据情况将请求路由到多个 Amazon Auto Scaling Groups（亚马逊自动缩放组，亚马逊的一种云计算方式）的能力

Zuul 中最关键的是 **Router（路由）** 和 **Filter（过滤器）**

> 关于 Zuul 的配置，可以参考 [springcloud-gateway-zuul-9900](./springcloud-gateway-zuul-9900) 的 [配置文件](./springcloud-gateway-zuul-9900/src/main/resources/application.yml) 

## Router：Zuul 的路由功能

### 路由基础

因为 Consumer(s) 都向 Eureka Server 进行注册了，所以 Zuul 网关只需要向 Eureka Server 注册，也能拿到所有 Consumer 的信息。

也就是说，**Zuul 要向 Eureka Server 注册，以获取所有 Consumer 的 Meta Data（元数据）**。

Zuul 获取所有 Consumer 的 Meta Data（名称、IP 和端口等）之后，就能做 **路由映射** 了。

起動するクラスファイルに `@EnableZuulProxy` アノテーションをつけます（参考：[Zuul9900.java](./springcloud-gateway-zuul-9900/src/main/java/com/example/springcloud/Zuul9900.java)）

最后加入配置即可，如：

```yml
server:
  port: 9000
eureka:
  client:
    service-url:
      # 这里只要注册 Eureka 就行了
      defaultZone: http://localhost:9997/eureka
```

这样的话，就可以通过 9000 的端口加上 `/项目名称` 再加上请求路径来访问了。

### 路由策略配置

自定义路由策略：

在 HTTP 请求中，如果是 Consumer 的名称为 name，那么请求链接可能就是 `localhost:9000/项目名称/...` 的样式，这样会把微服务的项目名称暴露出来，存在安全风险。

所以，需要自定义路径来替代微服务的名称，配置如下

```yml
zuul:
  routes:
    name: /DirName/**
```

这个时候，就可以使用 `localhost:9000/项目名称/DirName/...` 来访问了。

不过，之前使用微服务名称的方式依旧可以访问，所以需要屏蔽所有服务名：

```yml
zuul:
  ignore-services: "*"
```

## Filter：Zuul 的过滤功能

Router 是 Zuul 的基础功能，Filter 是 Zuul 的进阶功能。因为所有请求都经过 Zuul 网关，所以通过过滤，可以实现限流、灰度发布和权限控制等。

### Zuul 的过滤器类型及请求的生命周期

过滤器类型及请求的生命周期：

* "Pre" Filters
  * Request 刚进来，还没有被路由（路由：route (verb)，表示按路线/映射路径发送）这段期间的过滤器
  * 实现身份验证、在集群中选择请求的微服务、记录调试信息等
* "Routing" Filter(s)
  * 将请求（request）根据「路由策略」，路由（routing）到微服务的过滤器
  * 用于创建“发送给微服务的请求”，并使用 Apache HttpClient 或 Netfilx Ribbon 请求微服务
* "Post" Filters
  * 在请求到达微服务之后，在 Response 之前执行过滤的过滤器
  * 用来为响应添加标准的 HTTP Header、收集统计信息和指标、将响应从微服务发送给客户端等
* "Error" Filter
  * 在所有阶段，发生错误时执行该过滤器

![](https://images2017.cnblogs.com/blog/285763/201709/285763-20170918111945728-545715096.png)

图：请求的生命周期及相应的过滤器

参考资料：[服务网关zuul之二：过滤器--请求过滤执行过程（源码分析）](https://www.cnblogs.com/duanxz/p/7542150.html)

### Zuul 屏蔽路径和请求头

**路径屏蔽**

只要用户请求中包含指定的 URI 路径，那么该请求将无法访问到指定的服务。

通过该方式可以<u>限制用户的权限</u>，也就是完成<u>权限控制</u>，配置如下：

```yml
zuul:
  ignore-patterns: **/auto/**
```

`**` 代表匹配多级任意路径
`*` 代表匹配一级任意路径

**敏感请求头屏蔽**

默认情况下，像 Cookie、Set-Cookie 等敏感请求头信息会被 Zuul 屏蔽掉。

我们可以将这些默认屏蔽去掉，当然，也可以添加要屏蔽的请求头。

### Zuul 的实际使用

**简单实现一个请求时间日志打印**

要实现自己定义的 Filter 我们只需要 `extends ZuulFilter`，然后将这个过滤器类用 `@Component` 注解加入 Spring 容器中。

实现请求时间日志打印功能的前置过滤器：

```java
// 加入Spring容器
@Component
public class PreRequestFilter extends ZuulFilter {

    // 返回过滤器类型 这里是前置过滤器
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    // 指定过滤顺序：越小越先执行，返回 0 表示第一个执行
    // 其实也不是真的第一个执行，因为在 Zuul 内置的其他过滤器会先执行
    // 内置的过滤器无法更改，比如：SERVLET_DETECTION_FILTER_ORDER = -3
    @Override
    public int filterOrder() {
        return 0;
    }

    // 制定判断规则，确定哪些请求是可以被过滤的，哪些又不能被过滤
    @Override
    public boolean shouldFilter() {
        return true;
    }

    // 如果过滤器允许通过，就要确定过滤时执行的策略
    @Override
    public Object run() throws ZuulException {
        // 这里设置了全局的RequestContext并记录了请求开始时间
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.set("startTime", System.currentTimeMillis());
        return null;
    }
}
```

实现请求时间日志打印功能的后置过滤器：

```java
@Slf4j
@Component
public class AccessLogFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    // SEND_RESPONSE_FILTER_ORDER 是最后一个过滤器
    // 比最后一个过滤器少 1 个单位，代表在最后一个过滤器之前执行
    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        // 从RequestContext获取原先的开始时间 并通过它计算整个时间间隔
        Long startTime = (Long) context.get("startTime");
        // 这里可以获取 HttpServletRequest 以获取 URI 并且打印出来
        String uri = request.getRequestURI();
        long duration = System.currentTimeMillis() - startTime;
        log.info("uri: " + uri + ", duration: " + duration / 100 + "ms");
        return null;
    }
}
```

---

**令牌桶限流**（其实不仅仅是令牌桶限流方式，Zuul 只要是限流的活它都能干）

令牌桶限流：

【令牌】--以固定速率放入-->【Token Bucket（桶）】

> 如果 Token Bucket 满了，那么新放入的令牌就会被丢弃

【Request】--成功从桶中获取令牌--> 放行
【Request】--没有成功从桶中获取令牌--> 拒绝

我们能将请求数量控制在一秒两个：

```java
@Component
@Slf4j
public class RouteFilter extends ZuulFilter {
    // 定义一个令牌桶，每秒产生2个令牌，即每秒最多处理2个请求
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(2);

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return -5;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("放行");
        return null;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        if(!RATE_LIMITER.tryAcquire()) {
            log.warn("访问量超载");
            // 指定当前请求未通过过滤
            context.setSendZuulResponse(false);
            // 向客户端返回响应码 429，请求数量过多
            context.setResponseStatusCode(429);
            return false;
        }
        return true;
    }
}
```

> Zuul 作为网关肯定也存在 *单点问题* ，如果我们要保证 Zuul 的高可用，我们就需要进行 Zuul 的集群配置，这个时候可以借助额外的一些负载均衡器比如 Nginx

# Spring Cloud Config：配置管理

## Spring Cloud Config

为什么要使用进行配置管理？

* 为了避免去每个应用下，单独寻找并修改配置文件，再重启应用的麻烦
  * 微服务系统中的 Consumer 、Provider 、Eureka Server、Zuul 系统都会持有自己的配置
  * 在项目运行的时候，可能需要更改某些应用的配置
* 重启应用来更新配置，会让服务无法访问，直接抛弃了可用性，所以对于分布式系统来说，根本就不应该去每个应用下去分别修改配置文件

解决方法：

* 既能对配置文件统一地进行管理，又能在项目运行时动态修改配置文件：
  * Spring Cloud Config
  * disconf
  * Apollo

Spring Cloud Config 能将各个应用/系统/模块的配置文件 **存放到统一的地方并进行管理** ：

* Spring Cloud Config 为分布式系统中的外部化配置提供服务器和客户端支持
* 使用 Config 服务器，可以在中心位置管理所有环境中应用程序的外部属性

> 「配置文件」是在启动的时候才被加载，「Spring Cloud Config」会提供一个「接口」给「用于启动的应用」
> 
> 「启动应用」从这个「接口」中获取需要的「配置文件」，然后再进行初始化工作

Spring Cloud Config 运作流程（Config 的客户端简称为 Client，Config 的服务器简称为 Server）：

* 【Client】--请求 URI-->【Server】
* 【Server】--获取配置-->【Git/SVN】
* 【Git】--发送配置-->【Server】
* 【Server】--返回配置-->【Client】

Spring Cloud Config 相关 GitHub 仓库和代码：

- Server（服务端）：[springcloud-config-server-3001](./springcloud-config-server-3001)
- Client（客户端，用于基础演示）：[springcloud-config-client-4001](./springcloud-config-client-4001)
- Client（客户端，用于在项目中使用）：
  - [springcloud-eureka-7001](./springcloud-eureka-7001) 的 [bootstrap.yml](./springcloud-eureka-7001/src/main/resources/bootstrap.yml)
  - [springcloud-provider-dept-8001](./springcloud-provider-dept-8001)  的 [bootstrap.yml](./springcloud-provider-dept-8001/src/main/resources/bootstrap.yml) 
- 在 Server [配置](./springcloud-config-server-3001/src/main/resources/application.yml) 了 [GitHub 配置仓库的链接](https://github.com/LearnDifferent/springcloud-config-demo/blob/master/application.yml) 后，可以通过以下链接访问该仓库的配置
  - http://localhost:3001/application-dev.yml
    - http://localhost:3001/application-test.yml
    - http://localhost:3001/application-prod.yml
    - http://localhost:3001/config-eureka-server.yml
    - http://localhost:3001/config-provider-dept.yml

参考资料：

- 推荐：[Spring Cloud Config 实现配置中心，看这一篇就够了](https://www.cnblogs.com/fengzheng/p/11242128.html)
- 官方的[这篇](https://spring.io/guides/gs/centralized-configuration/)和[这篇文档](https://cloud.spring.io/spring-cloud-config/reference/html/#_spring_cloud_config_server)

## Spring Cloud Bus

为什么要使用 Spring Cloud Bus？

* 如果使用 Git 修改对应的配置文件，那么已经启动的应用能不能直接识别更改，并进行相应的配置呢？其实不行，还是会出现*配置漂移*的情况
* 这时就要使用 GitHub 推出的 Webhooks 来确保远程库的配置文件更新后，Spring Cloud Config 客户端中的配置信息也得到更新，但是这种方案不适合用于生产环境，所以基本不用
* 一般我们会使用 Bus 消息总线 + Spring Cloud Config 进行配置的动态刷新

Spring Cloud Bus：

* 是<u>将服务和服务实例与分布式消息系统链接在一起</u>的事件总线/消息总线
* 主要用于在集群中，传播状态的更改（例如配置更改事件）

Spring Cloud Bus 作用：

* 管理和广播分布式系统中的消息
* 也就是消息引擎系统中的广播模式

拥有了 Spring Cloud Bus 之后，我们只需要创建一个简单的请求，并且加上 @ResfreshScope 注解就能进行配置的动态修改了

![](http://blog.didispace.com/assets/5-6.png)

> 图：Spring Cloud Bus 流程图（来自：[程序猿DD](http://blog.didispace.com/)）