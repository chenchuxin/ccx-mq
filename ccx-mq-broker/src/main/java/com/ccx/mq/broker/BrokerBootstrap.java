package com.ccx.mq.broker;

import cn.hutool.setting.Setting;
import com.ccx.mq.remoting.protocol.netty.server.NettyServer;
import com.ccx.mq.remoting.protocol.netty.server.NettyServerConfig;

/**
 * broker 启动类
 *
 * @author chenchuxin
 * @date 2021/8/28
 */
public class BrokerBootstrap {

    public static void main(String[] args) {
        //noinspection MismatchedQueryAndUpdateOfCollection
        Setting setting = new Setting("broker.conf");
        NettyServerConfig config = new NettyServerConfig();
        config.setListenPort(setting.getInt("server.port"));
        NettyServer nettyServer = new NettyServer(config);
        nettyServer.start();
    }
}
