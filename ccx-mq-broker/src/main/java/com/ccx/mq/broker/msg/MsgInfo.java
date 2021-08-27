package com.ccx.mq.broker.msg;

import lombok.Data;

/**
 * 消息
 *
 * @author chenchuxin
 * @date 2021/8/27
 */
@Data
public class MsgInfo {
    private String topic;
    private String msg;
}
