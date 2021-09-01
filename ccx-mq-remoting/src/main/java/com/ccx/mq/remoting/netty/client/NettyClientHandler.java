package com.ccx.mq.remoting.netty.client;

import com.ccx.mq.remoting.consts.CommandCode;
import com.ccx.mq.remoting.consts.CommandType;
import com.ccx.mq.remoting.consts.CompressType;
import com.ccx.mq.remoting.consts.SerializeType;
import com.ccx.mq.remoting.protocol.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

/**
 * netty 客户端处理
 *
 * @author chenchuxin
 * @date 2021/8/27
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<Command> {

    private final RequestFutureManager requestFutureManager = new RequestFutureManager();

    /**
     * 请求 Id
     */
    private static final AtomicLong REQUEST_ID = new AtomicLong(0);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Command cmd) {
        try {
            if (cmd.getCommandType() == CommandType.RESPONSE.getValue()) {
                requestFutureManager.processResponseCommand(ctx, cmd);
            }
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
                log.info("write idle happen [{}, {}]", ctx.channel().localAddress(), ctx.channel().remoteAddress());
                Channel channel = ctx.channel();
                Command cmd = Command.builder().commandType(CommandType.HEARTBEAT.getValue())
                        .commandCode(0)
                        .serializerType(SerializeType.PROTOSTUFF.getValue())
                        .compressorType(CompressType.GZIP.getValue()).build();
                channel.writeAndFlush(cmd).addListener((ChannelFutureListener) future -> {
                    if (!future.isSuccess()) {
                        future.channel().close();
                    }
                });
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

    /**
     * 发请求
     *
     * @param channel 渠道
     * @param body    消息体
     */
    public CompletableFuture<Command> request(Channel channel, Object body, CommandCode commandCode) {
        Command requestCommand = new Command();
        long requestId = createRequestId();
        requestCommand.setRequestId(requestId);
        requestCommand.setCommandCode(commandCode.getCode());
        requestCommand.setCommandType(CommandType.REQUEST.getValue());
        requestCommand.setSerializerType(SerializeType.PROTOSTUFF.getValue());
        requestCommand.setCompressorType(CompressType.GZIP.getValue());
        requestCommand.setBody(body);

        CompletableFuture<Command> resultFuture = new CompletableFuture<>();
        requestFutureManager.putRequest(requestId, resultFuture);
        channel.writeAndFlush(requestCommand).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("client send message: [{}]", body);
            } else {
                future.channel().close();
                resultFuture.completeExceptionally(future.cause());
                log.error("send failed:", future.cause());
            }
        });
        return resultFuture;
    }

    public Long createRequestId() {
        return REQUEST_ID.getAndIncrement();
    }

}
