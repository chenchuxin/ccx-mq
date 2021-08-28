package com.ccx.mq.remoting.protocol.netty.processor;

import cn.hutool.json.JSONUtil;
import com.ccx.mq.remoting.protocol.Command;
import com.ccx.mq.remoting.protocol.consts.CommandFrameConst;
import com.ccx.mq.remoting.protocol.consts.CommandType;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * netty 处理器管理
 *
 * @author chenchuxin
 * @date 2021/8/27
 */
@Slf4j
public class NettyProcessorManager {

    public static final NettyProcessorManager INSTANT = new NettyProcessorManager();

    private NettyProcessorManager() {
    }

    /**
     * 处理器注册表。{命令码: 处理器}
     */
    private static final Map<Integer, NettyProcessor> PROCESSOR_MAP = new ConcurrentHashMap<>();

    /**
     * 结果列表。{requestId: 处理结果}
     */
    private static final Map<Long, CompletableFuture<Command>> FUTURE_MAP = new ConcurrentHashMap<>();

    /**
     * 注册处理器
     *
     * @param commandCode 命令码
     * @param processor   处理器
     */
    public void registerProcessor(int commandCode, NettyProcessor processor) {
        PROCESSOR_MAP.put(commandCode, processor);
    }

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
     * 处理命令。会根据命令是请求或者响应来处理
     *
     * @param ctx 上下文
     * @param cmd 命令
     */
    public void processCommand(ChannelHandlerContext ctx, Command cmd) {
        if (cmd.getCommandType() == CommandType.REQUEST.getValue()) {
            processRequestCommand(ctx, cmd);
        } else if (cmd.getCommandType() == CommandType.RESPONSE.getValue()) {
            processResponseCommand(ctx, cmd);
        }
        // 心跳类型不管
    }

    /**
     * 根据命令码获取处理器
     *
     * @param commandCode 命令码
     * @return 处理器，如果获取不到，返回 null
     */
    private NettyProcessor getProcessor(int commandCode) {
        return PROCESSOR_MAP.get(commandCode);
    }

    /**
     * 处理请求命令
     *
     * @param ctx     上下文
     * @param request 请求命令
     */
    private void processRequestCommand(ChannelHandlerContext ctx, Command request) {
        // 从注册表中拿到对应的处理器
        NettyProcessor processor = getProcessor(request.getCommandCode());
        if (processor == null) {
            throw new IllegalStateException("Unknown command code:" + request.getCommandCode());
        }

        try {
            Command response = processor.process(ctx, request);
            response.setRequestId(request.getRequestId());
            response.setCommandType(CommandType.RESPONSE.getValue());
            response.setCommandCode(request.getCommandCode());
            response.setCompressorType(request.getCompressorType());
            response.setSerializerType(request.getSerializerType());
            response.setVersion(CommandFrameConst.VERSION);
            ctx.writeAndFlush(response);
        } catch (Throwable e) {
            log.error("process request error. cmd={}", request, e);
        }
    }

    /**
     * 处理响应命令
     *
     * @param ctx      上下文
     * @param response 响应
     */
    private void processResponseCommand(ChannelHandlerContext ctx, Command response) {
        CompletableFuture<Command> future = FUTURE_MAP.remove(response.getRequestId());
        if (future != null) {
            future.complete(response);
        } else {
            log.warn("Process response command error. address:{}, response:{}", ctx.channel().remoteAddress(), JSONUtil.toJsonStr(response));
        }
    }
}
