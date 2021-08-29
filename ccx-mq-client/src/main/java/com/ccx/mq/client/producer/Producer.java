package com.ccx.mq.client.producer;

import com.ccx.mq.remoting.protocol.Command;
import com.ccx.mq.remoting.protocol.body.SendMsgRequest;
import com.ccx.mq.remoting.protocol.netty.client.NettyClient;
import com.ccx.mq.remoting.protocol.netty.client.NettyClientConfig;
import io.netty.channel.Channel;
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

    private InetSocketAddress brokerAddress;

    private NettyClient nettyClient;

    public Producer(String host, int port) {
        brokerAddress = new InetSocketAddress(host, port);
    }

    public Producer(String addressStr) {
        String[] hostAndPort = addressStr.split(":");
        String host = hostAndPort[0];
        int port = Integer.parseInt(hostAndPort[1]);
        brokerAddress = new InetSocketAddress(host, port);
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
     * @return 结果，请求失败可能是 null
     */
    public Command sendMsg(String topic, String msg) {
        Channel channel = nettyClient.getChannel(brokerAddress);

        SendMsgRequest request = new SendMsgRequest();
        request.setTopic(topic);
        request.setMessage(msg);

        // TODO：重试
        CompletableFuture<Command> future = nettyClient.request(channel, request);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("send error. topic={}, msg={}", topic, msg, e);
        }
        return null;
    }
}
