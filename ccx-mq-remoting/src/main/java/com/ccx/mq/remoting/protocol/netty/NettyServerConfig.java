package com.ccx.mq.remoting.protocol.netty;

import lombok.Data;

/**
 * netty 服务器配置
 *
 * @author chenchuxin
 * @date 2021/8/26
 */
@Data
public class NettyServerConfig {

    /**
     * 监听的端口
     */
    private int listenPort;
}
