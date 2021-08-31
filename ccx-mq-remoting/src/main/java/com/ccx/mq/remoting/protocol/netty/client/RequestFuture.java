package com.ccx.mq.remoting.protocol.netty.client;

import cn.hutool.json.JSONUtil;
import com.ccx.mq.remoting.protocol.Command;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenchuxin
 * @date 2021/8/31
 */
@Slf4j
public class RequestFuture {

    /**
     * 结果列表。{requestId: 处理结果}
     */
    private static final Map<Long, CompletableFuture<Command>> FUTURE_MAP = new ConcurrentHashMap<>();

    /**
     * 加入请求
     *
     * @param requestId 请求 id
     * @param future    结果等待
     */
    public void putRequest(long requestId, CompletableFuture<Command> future) {
        FUTURE_MAP.put(requestId, future);
    }

    /**
     * 处理响应命令
     *
     * @param ctx      上下文
     * @param response 响应
     */
    public void processResponseCommand(ChannelHandlerContext ctx, Command response) {
        CompletableFuture<Command> future = FUTURE_MAP.remove(response.getRequestId());
        if (future != null) {
            future.complete(response);
        } else {
            log.warn("Process response command error. address:{}, response:{}", ctx.channel().remoteAddress(), JSONUtil.toJsonStr(response));
        }
    }
}
