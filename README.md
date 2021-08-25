# ccx-mq

### 介绍
为了学习而造的 mq 轮子。

### 版本规划
#### 1.0.0
- [ ] 初始架构：broker、client(producer、consumer)
- [ ] 要求多 producer 能生产消息，多 consumer 能消费消息
- [ ] 支持 topic(主题)、group(消费者组)，暂不支持分区/多队列
- [ ] broker 和 client 独立部署，用网络请求生产/消费消息
- [ ] broker 先用单机模式，暂不支持集群
- [ ] 消息存放到内存，暂不持久化
- [ ] 暂不开发 nameserver / 注册中心，客户端和 broker 先直连
- [ ] 消费模型只支持拉模式，暂不支持推模式

#### 参与贡献
1.  Fork 本仓库
2.  新建分支
3.  提交代码
4.  新建 Pull Request
