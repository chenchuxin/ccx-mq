package com.ccx.mq.broker.processor;

import com.ccx.mq.broker.msg.MemoryMsgStore;
import com.ccx.mq.broker.msg.StoreMsgInfo;
import com.ccx.mq.remoting.protocol.Command;
import com.ccx.mq.remoting.protocol.body.PullMsgRequest;
import com.ccx.mq.remoting.protocol.body.PullMsgResponse;
import com.ccx.mq.remoting.protocol.netty.processor.NettyProcessor;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 拉消息
 *
 * @author chenchuxin
 * @date 2021/8/29
 */
public class PullMsgProcessor implements NettyProcessor {

    private final MemoryMsgStore memoryMsgStore = MemoryMsgStore.INSTANT;

    @Override
    public Command process(ChannelHandlerContext ctx, Command cmd) {
        PullMsgRequest request = (PullMsgRequest) cmd.getBody();
        List<StoreMsgInfo> storeMsgInfos = memoryMsgStore.pullMessage(request.getTopic());
        PullMsgResponse response = new PullMsgResponse();
        List<String> msgList = storeMsgInfos.stream().map(StoreMsgInfo::getMsg).collect(Collectors.toList());
        response.setMessages(msgList);
        return Command.builder().body(response).build();
    }
}
