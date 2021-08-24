package com.ccx.mq.remoting.protocol.body;

import lombok.Data;

/**
 * 发送消息的请求
 *
 * @author chenchuxin
 * @date 2021/8/24
 */
@Data
public class SendMsgRequest {

    /**
     * 话题
     */
    private String topic;

    /**
     * 消息体
     */
    private String message;
}
