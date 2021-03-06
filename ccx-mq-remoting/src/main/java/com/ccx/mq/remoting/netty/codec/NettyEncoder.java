package com.ccx.mq.remoting.netty.codec;

import com.ccx.mq.remoting.protocol.Command;
import com.ccx.mq.remoting.protocol.codec.CommandEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * netty 编码器
 *
 * @author chenchuxin
 * @date 2021/8/26
 */
public class NettyEncoder extends MessageToByteEncoder<Command> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Command cmd, ByteBuf out) {
        CommandEncoder.INSTANT.encode(cmd, out);
    }
}
