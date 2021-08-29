package com.ccx.mq.broker.processor;

import com.ccx.mq.broker.msg.MemoryMsgStore;
import com.ccx.mq.broker.msg.MsgStore;
import com.ccx.mq.broker.msg.StoreMsgInfo;
import com.ccx.mq.common.MsgInfo;
import com.ccx.mq.common.ResponseMsgInfo;
import com.ccx.mq.common.SingletonFactory;
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

    private final MsgStore msgStore = SingletonFactory.getSingleton(MemoryMsgStore.class);

    @Override
    public Command process(ChannelHandlerContext ctx, Command cmd) {
        PullMsgRequest request = (PullMsgRequest) cmd.getBody();
        List<StoreMsgInfo> storeMsgInfos = msgStore.pullMessage(request.getTopic(), request.getCount());
        PullMsgResponse response = new PullMsgResponse();
        List<ResponseMsgInfo> msgList = storeMsgInfos.stream().map(msg -> {
            ResponseMsgInfo msgInfo = new ResponseMsgInfo();
            msgInfo.setMsg(msg.getMsg());
            msgInfo.setOffset(msg.getOffset());
            return msgInfo;
        }).collect(Collectors.toList());
        response.setMessages(msgList);
        return Command.builder().body(response).build();
    }
}
