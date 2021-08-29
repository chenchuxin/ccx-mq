package com.ccx.mq.broker.processor;

import cn.hutool.core.util.StrUtil;
import com.ccx.mq.broker.msg.*;
import com.ccx.mq.broker.topic.TopicManager;
import com.ccx.mq.common.MsgInfo;
import com.ccx.mq.common.SingletonFactory;
import com.ccx.mq.remoting.protocol.Command;
import com.ccx.mq.remoting.protocol.body.SendMsgRequest;
import com.ccx.mq.remoting.protocol.body.SendMsgResponse;
import com.ccx.mq.remoting.protocol.consts.CommandCode;
import com.ccx.mq.remoting.protocol.consts.ResponseCode;
import com.ccx.mq.remoting.protocol.netty.processor.NettyProcessor;
import io.netty.channel.ChannelHandlerContext;

/**
 * 发消息处理器
 *
 * @author chenchuxin
 * @date 2021/8/27
 */
public class SendMsgProcessor implements NettyProcessor {

    private final MsgStore msgStore = SingletonFactory.getSingleton(MemoryMsgStore.class);
    private final TopicManager topicManager = SingletonFactory.getSingleton(TopicManager.class);

    @Override
    public Command process(ChannelHandlerContext ctx, Command cmd) {
        CommandCode commandCode = CommandCode.fromCode(cmd.getCommandCode());
        if (commandCode == null) {
            throw new IllegalStateException("Unknown commandCode:" + cmd.getCommandCode());
        }
        SendMsgRequest sendMsgRequest = (SendMsgRequest) cmd.getBody();
        String topic = sendMsgRequest.getTopic();
        SendMsgResponse response = new SendMsgResponse();
        if (StrUtil.isBlank(topic)) {
            response.setCode(ResponseCode.TOPIC_NOT_VALID.getCode());
            response.setRemark(ResponseCode.TOPIC_NOT_VALID.getRemark());
            return Command.builder().body(response).build();
        }
        topicManager.createIfNeed(topic);
        String message = sendMsgRequest.getMessage();
        final MsgInfo msgInfo = new MsgInfo();
        msgInfo.setTopic(topic);
        msgInfo.setMsg(message);
        PutMsgResult putMsgResult = msgStore.putMessage(msgInfo);
        response.setOffset(putMsgResult.getOffset());
        if (putMsgResult.getPutMsgStatus() == PutMsgStatus.SUCCESS) {
            return Command.builder().body(response).build();
        }
        return null;
    }
}
