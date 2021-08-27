package com.ccx.mq.remoting.protocol.body;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 发送消息的响应
 *
 * @author chenchuxin
 * @date 2021/8/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SendMsgResponse extends BaseResponse {

    /**
     * 偏移量
     */
    private long offset;
}
