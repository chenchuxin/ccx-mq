package com.ccx.mq.broker.processor;

import com.ccx.mq.broker.offset.OffsetManager;
import com.ccx.mq.common.SingletonFactory;
import com.ccx.mq.remoting.protocol.Command;
import com.ccx.mq.remoting.protocol.body.UpdateOffsetRequest;
import com.ccx.mq.remoting.protocol.body.UpdateOffsetResponse;
import com.ccx.mq.remoting.protocol.consts.CommandCode;
import com.ccx.mq.remoting.protocol.netty.processor.NettyProcessor;
import io.netty.channel.ChannelHandlerContext;

/**
 * 更新偏移量
 *
 * @author chenchuxin
 * @date 2021/8/29
 */
public class UpdateOffsetProcessor implements NettyProcessor {

    private final OffsetManager offsetManager = SingletonFactory.getSingleton(OffsetManager.class);

    @Override
    public Command process(ChannelHandlerContext ctx, Command cmd) {
        CommandCode commandCode = CommandCode.fromCode(cmd.getCommandCode());
        if (commandCode == null) {
            throw new IllegalStateException("Unknown commandCode:" + cmd.getCommandCode());
        }
        UpdateOffsetRequest request = (UpdateOffsetRequest) cmd.getBody();
        offsetManager.updateOffset(request.getTopic(), request.getOffset());
        return Command.builder().body(new UpdateOffsetResponse()).build();
    }
}
