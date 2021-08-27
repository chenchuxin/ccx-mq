package com.ccx.mq.broker.processor;

import cn.hutool.core.util.StrUtil;
import com.ccx.mq.broker.topic.TopicManager;
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

    @Override
    public Command process(ChannelHandlerContext ctx, Command cmd) throws Exception {
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
        TopicManager.INSTANT.createIfNeed(topic);
        sendMsgRequest.getMessage();
        return null;
    }
}
