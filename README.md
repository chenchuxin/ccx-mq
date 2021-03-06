# ccx-mq

### 介绍
为了学习而造的 mq 轮子。

### 版本规划
#### 1.0.0
- [x] 初始架构：broker、client(producer、consumer)
- [x] 支持 topic(主题)，暂不支持消费者/分区/多队列
- [x] broker 和 client 独立部署，用网络请求生产/消费消息
- [x] broker 先用单机模式，暂不支持集群
- [x] 消息存放到内存，暂不持久化
- [x] 暂不开发 nameserver / 注册中心，客户端和 broker 先直连

#### 1.1.0
- [ ] 消息使用硬盘持久化

### 如何运行
1. 启动 broker：`ccx-mq-broker` 的 `BrokerBootstrap`
2. 启动客户端 Demo：`ccx-mq-demo` 的 `DemoBootstrap`，是一个 Web
3. 调用 `http://localhost:8080/producer/send?msg=hello` 发送消息
4. 调用 `http://localhost:8080/consumer/pull?count=10` 拉消息
5. 调用 `http://localhost:8080/consumer/updateOffset?offset=1` 更新位移

#### 参与贡献
1.  Fork 本仓库
2.  新建分支
3.  提交代码
4.  新建 Pull Request
