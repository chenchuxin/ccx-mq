package com.ccx.mq.client.producer;

import com.ccx.mq.remoting.protocol.Command;
import com.ccx.mq.remoting.protocol.body.SendMsgRequest;
import com.ccx.mq.remoting.protocol.body.SendMsgResponse;
import com.ccx.mq.remoting.protocol.compress.Compressor;
import com.ccx.mq.remoting.protocol.consts.*;
import com.ccx.mq.remoting.protocol.netty.client.NettyClient;
import com.ccx.mq.remoting.protocol.netty.client.NettyClientConfig;
import com.ccx.mq.remoting.protocol.netty.processor.NettyProcessorManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 生产者
 *
 * @author chenchuxin
 * @date 2021/8/29
 */
@Slf4j
public class Producer {

    private String brokerAddress;

    private NettyClient nettyClient;

    public Producer(String brokerAddress) {
        this.brokerAddress = brokerAddress;
    }

    /**
     * 启动
     */
    public void start() {
        NettyClientConfig config = new NettyClientConfig();
        // 生产者需要保持长连接
        config.setKeepAlive(true);
        nettyClient = new NettyClient(config);
    }

    /**
     * 发送消息
     *
     * @param topic 主题
     * @param msg   消息
     */
    public void sendMsg(String topic, String msg) {
        String[] hostAndPort = brokerAddress.split(":");
        InetSocketAddress address = new InetSocketAddress(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
        Channel channel = nettyClient.getChannel(address);

        SendMsgRequest request = new SendMsgRequest();
        request.setTopic(topic);
        request.setMessage(msg);

        // TODO：重试
        CompletableFuture<Command> future = nettyClient.request(channel, request);
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("send error. topic={}, msg={}", topic, msg, e);
        }
    }
}
