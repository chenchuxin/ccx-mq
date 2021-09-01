package com.ccx.mq.client.consumer;

import com.ccx.mq.common.ResponseMsgInfo;
import com.ccx.mq.remoting.consts.CommandCode;
import com.ccx.mq.remoting.netty.client.NettyClient;
import com.ccx.mq.remoting.netty.client.NettyClientConfig;
import com.ccx.mq.remoting.protocol.Command;
import com.ccx.mq.remoting.protocol.body.PullMsgRequest;
import com.ccx.mq.remoting.protocol.body.PullMsgResponse;
import com.ccx.mq.remoting.protocol.body.UpdateOffsetRequest;
import com.ccx.mq.remoting.protocol.body.UpdateOffsetResponse;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 消费者
 *
 * @author chenchuxin
 * @date 2021/8/29
 */
@Slf4j
public class Consumer {

    private final InetSocketAddress brokerAddress;

    private NettyClient nettyClient;

    public Consumer(String host, int port) {
        brokerAddress = new InetSocketAddress(host, port);
    }

    public Consumer(String addressStr) {
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
     * 拉取消息
     *
     * @param topic 主题
     * @param count 数量
     * @return 如果拉取不到，返回空列表
     */
    public List<ResponseMsgInfo> pull(String topic, int count) {
        PullMsgRequest request = new PullMsgRequest();
        request.setTopic(topic);
        request.setCount(count);
        Channel channel = nettyClient.getChannel(brokerAddress);
        CompletableFuture<Command> future = nettyClient.request(channel, request, CommandCode.PULL_MSG);
        try {
            Command responseCommand = future.get();
            if (responseCommand != null) {
                PullMsgResponse pullMsgResponse = (PullMsgResponse) responseCommand.getBody();
                return pullMsgResponse.getMessages();
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("pull error. topic={}, count={}", topic, count, e);
        }
        return Collections.emptyList();
    }

    /**
     * 更新偏移量
     *
     * @param topic  主题
     * @param offset 偏移量
     * @return 响应码
     */
    public int updateOffset(String topic, long offset) {
        UpdateOffsetRequest request = new UpdateOffsetRequest();
        request.setTopic(topic);
        request.setOffset(offset);
        Channel channel = nettyClient.getChannel(brokerAddress);
        CompletableFuture<Command> future = nettyClient.request(channel, request, CommandCode.UPDATE_OFFSET);
        try {
            Command responseCommand = future.get();
            if (responseCommand != null) {
                UpdateOffsetResponse pullMsgResponse = (UpdateOffsetResponse) responseCommand.getBody();
                return pullMsgResponse.getCode();
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("pull error. topic={}, offset={}", topic, offset, e);
        }
        return 0;
    }
}
