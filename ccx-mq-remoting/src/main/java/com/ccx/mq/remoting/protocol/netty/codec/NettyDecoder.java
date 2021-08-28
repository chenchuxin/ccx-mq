package com.ccx.mq.remoting.protocol.netty.codec;

import com.ccx.mq.remoting.protocol.codec.CommandCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import static com.ccx.mq.remoting.protocol.consts.CommandFrameConst.*;

/**
 * netty 的解码器
 *
 * @author chenchuxin
 * @date 2021/8/26
 */
@Slf4j
public class NettyDecoder extends LengthFieldBasedFrameDecoder {

    public NettyDecoder() {
        // initialBytesToStrip: 因为我们还需要检测 魔法数 和 版本号，所以不能截掉
        super(MAX_FRAME_LENGTH, MAGIC_LENGTH + VERSION_LENGTH, FULL_LENGTH_LENGTH,
                -(MAGIC_LENGTH + VERSION_LENGTH + FULL_LENGTH_LENGTH), 0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decoded;
            if (frame.readableBytes() >= HEADER_LENGTH) {
                try {
                    return CommandCodec.INSTANT.decode(frame);
                } catch (Exception ex) {
                    log.error("Decode frame error.", ex);
                } finally {
                    frame.release();
                }
            }
        }
        return decoded;
    }
}
