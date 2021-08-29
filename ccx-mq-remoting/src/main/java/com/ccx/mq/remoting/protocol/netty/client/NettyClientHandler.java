package com.ccx.mq.remoting.protocol.netty.client;

import com.ccx.mq.common.SingletonFactory;
import com.ccx.mq.remoting.protocol.Command;
import com.ccx.mq.remoting.protocol.consts.CommandType;
import com.ccx.mq.remoting.protocol.consts.CompressType;
import com.ccx.mq.remoting.protocol.consts.SerializeType;
import com.ccx.mq.remoting.protocol.netty.processor.NettyProcessorManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * netty 客户端处理
 *
 * @author chenchuxin
 * @date 2021/8/27
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<Command> {

    private final NettyProcessorManager processorManager = SingletonFactory.getSingleton(NettyProcessorManager.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Command cmd) {
        try {
            processorManager.processCommand(ctx, cmd);
        } finally {
            ReferenceCountUtil.release(cmd);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 心跳
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                log.info("write idle happen [{}]", ctx.channel().remoteAddress());
                Channel channel = ctx.channel();
                Command cmd = Command.builder().commandType(CommandType.HEARTBEAT.getValue())
                        .serializerType(SerializeType.PROTOSTUFF.getValue())
                        .compressorType(CompressType.GZIP.getValue()).build();
                channel.writeAndFlush(cmd).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * Called when an exception occurs in processing a client message
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("client catch exception：", cause);
        cause.printStackTrace();
        ctx.close();
    }

}
