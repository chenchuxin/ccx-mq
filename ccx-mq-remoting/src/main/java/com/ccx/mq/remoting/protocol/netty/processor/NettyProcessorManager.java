package com.ccx.mq.remoting.protocol.netty.processor;

import com.ccx.mq.remoting.protocol.Command;
import com.ccx.mq.remoting.protocol.consts.CommandFrameConst;
import com.ccx.mq.remoting.protocol.consts.CommandType;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * netty 处理器管理
 *
 * @author chenchuxin
 * @date 2021/8/27
 */
@Slf4j
public class NettyProcessorManager {

    /**
     * 处理器注册表。{命令码: 处理器}
     */
    private static final Map<Integer, NettyProcessor> PROCESSOR_MAP = new ConcurrentHashMap<>();

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
     * 根据命令码获取处理器
     *
     * @param commandCode 命令码
     * @return 处理器，如果获取不到，返回 null
     */
    private NettyProcessor getProcessor(int commandCode) {
        return PROCESSOR_MAP.get(commandCode);
    }

    /**
     * 处理命令
     *
     * @param ctx     上下文
     * @param request 请求命令
     */
    public void process(ChannelHandlerContext ctx, Command request) {
        // 从注册表中拿到对应的处理器
        NettyProcessor processor = getProcessor(request.getCommandCode());
        if (processor == null) {
            throw new IllegalStateException("Unknown command code:" + request.getCommandCode());
        }

        try {
            Command responseCommand = processor.process(ctx, request);
            responseCommand.setRequestId(request.getRequestId());
            responseCommand.setCommandType(CommandType.RESPONSE.getValue());
            responseCommand.setCommandCode(request.getCommandCode());
            responseCommand.setCompressorType(request.getCompressorType());
            responseCommand.setSerializerType(request.getSerializerType());
            responseCommand.setVersion(CommandFrameConst.VERSION);
            ctx.writeAndFlush(responseCommand);
        } catch (Throwable e) {
            log.error("process request error. cmd={}", request, e);
        }
    }
}
