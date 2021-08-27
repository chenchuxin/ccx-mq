package com.ccx.mq.remoting.protocol.netty.processor;

import com.ccx.mq.remoting.protocol.Command;
import io.netty.channel.ChannelHandlerContext;

/**
 * 抽象异步处理器
 */
public abstract class AsyncNettyProcessor implements NettyProcessor {

    /**
     * 异步处理
     *
     * @param ctx      上下文
     * @param cmd      命令
     * @param callback 回调
     * @throws Exception 异常
     */
    public void asyncProcess(ChannelHandlerContext ctx, Command cmd, ResponseCallback callback) throws Exception {
        Command response = process(ctx, cmd);
        callback.callback(response);
    }
}
