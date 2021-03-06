package com.ccx.mq.remoting.netty.server;

import com.ccx.mq.remoting.consts.CommandType;
import com.ccx.mq.remoting.netty.processor.NettyProcessorManager;
import com.ccx.mq.remoting.protocol.Command;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务器处理器
 *
 * @author chenchuxin
 * @date 2021/8/26
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<Command> {

    private final NettyProcessorManager processorManager = new NettyProcessorManager();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Command cmd) {
        try {
            if (cmd.getCommandType() == CommandType.REQUEST.getValue()) {
                processorManager.process(ctx, cmd);
            }
        } finally {
            ReferenceCountUtil.release(cmd);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 处理空闲状态的
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.info("idle check happen, so close the connection");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
