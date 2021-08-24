package com.ccx.mq.remoting.protocol.body;

import lombok.Data;

/**
 * 拉消息请求
 *
 * @author chenchuxin
 * @date 2021/8/24
 */
@Data
public class PullMsgRequest {
    private String topic;
    /**
     * 拉取的数量
     */
    private int count;
}
