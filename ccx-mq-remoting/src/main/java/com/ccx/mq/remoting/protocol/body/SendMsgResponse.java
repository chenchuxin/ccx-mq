package com.ccx.mq.remoting.protocol.body;

import lombok.Data;

/**
 * 发送消息的响应
 *
 * @author chenchuxin
 * @date 2021/8/24
 */
@Data
public class SendMsgResponse {

    /**
     * 偏移量
     */
    private long offset;
}
