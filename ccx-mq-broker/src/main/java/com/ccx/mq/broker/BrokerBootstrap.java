package com.ccx.mq.broker;

import cn.hutool.setting.Setting;
import com.ccx.mq.broker.processor.PullMsgProcessor;
import com.ccx.mq.broker.processor.SendMsgProcessor;
import com.ccx.mq.broker.processor.UpdateOffsetProcessor;
import com.ccx.mq.common.SingletonFactory;
import com.ccx.mq.remoting.protocol.consts.CommandCode;
import com.ccx.mq.remoting.protocol.netty.processor.NettyProcessorManager;
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

        // 注册 Processor
        NettyProcessorManager processorManager = SingletonFactory.getSingleton(NettyProcessorManager.class);
        processorManager.registerProcessor(CommandCode.SEND_MSG.getCode(), new SendMsgProcessor());
        processorManager.registerProcessor(CommandCode.PULL_MSG.getCode(), new PullMsgProcessor());
        processorManager.registerProcessor(CommandCode.UPDATE_OFFSET.getCode(), new UpdateOffsetProcessor());

        // 启动
        NettyServerConfig config = new NettyServerConfig();
        config.setListenPort(setting.getInt("server.port"));
        NettyServer nettyServer = new NettyServer(config);
        nettyServer.start();
    }
}
