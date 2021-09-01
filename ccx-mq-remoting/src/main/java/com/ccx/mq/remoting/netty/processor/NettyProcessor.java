package com.ccx.mq.remoting.netty.processor;

import com.ccx.mq.remoting.protocol.Command;
import io.netty.channel.ChannelHandlerContext;

/**
 * 请求处理器，其他模块可以使用处理器来处理请求
 *
 * @author chenchuxin
 * @date 2021/8/26
 */
public interface NettyProcessor {

    /**
     * 处理
     *
     * @param ctx 上下文
     * @param cmd 命令
     * @return 响应
     * @throws Exception 异常
     */
    Command process(ChannelHandlerContext ctx, Command cmd) throws Exception;
}
