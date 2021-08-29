package com.ccx.mq.broker.msg;

import lombok.Builder;
import lombok.Data;

/**
 * 写入消息结果
 *
 * @author chenchuxin
 * @date 2021/8/27
 */
@Data
@Builder
public class PutMsgResult {
    /**
     * 写消息的状态
     */
    private PutMsgStatus putMsgStatus;

    /**
     * 该消息的偏移量
     */
    private long offset;
}
