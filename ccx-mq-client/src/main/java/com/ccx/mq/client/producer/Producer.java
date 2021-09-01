package com.ccx.mq.client.producer;

import com.ccx.mq.remoting.consts.CommandCode;
import com.ccx.mq.remoting.netty.client.NettyClient;
import com.ccx.mq.remoting.netty.client.NettyClientConfig;
import com.ccx.mq.remoting.protocol.Command;
import com.ccx.mq.remoting.protocol.body.SendMsgRequest;
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

    private final InetSocketAddress brokerAddress;

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

        SendMsgRequest request = new SendMsgRequest();
        request.setTopic(topic);
        request.setMessage(msg);

        // TODO：重试
        Channel channel = nettyClient.getChannel(brokerAddress);
        CompletableFuture<Command> future = nettyClient.request(channel, request, CommandCode.SEND_MSG);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("send error. topic={}, msg={}", topic, msg, e);
        }
        return null;
    }
}
