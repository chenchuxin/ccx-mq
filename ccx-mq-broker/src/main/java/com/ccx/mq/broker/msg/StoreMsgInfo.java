package com.ccx.mq.broker.msg;

import lombok.Data;

/**
 * 用于存储的消息
 *
 * @author chenchuxin
 * @date 2021/8/28
 */
@Data
public class StoreMsgInfo {
    private int offset;
    private String topic;
    private String msg;
}
