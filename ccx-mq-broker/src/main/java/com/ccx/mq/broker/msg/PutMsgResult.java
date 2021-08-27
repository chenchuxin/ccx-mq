package com.ccx.mq.broker.msg;

import lombok.AllArgsConstructor;
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
    private PutMsgStatus putMsgStatus;
}
