package com.ccx.mq.remoting.protocol.netty.client;

import lombok.Data;

/**
 * netty 客户端参数
 *
 * @author chenchuxin
 * @date 2021/8/29
 */
@Data
public class NettyClientConfig {
    /**
     * 是否保持长连接（生产者、消费者保持长连接，但是后台就不需要了）
     */
    private boolean keepAlive;


}
