package com.ccx.mq.remoting.protocol.body;

import com.ccx.mq.common.ResponseMsgInfo;
import lombok.Data;

import java.util.List;

/**
 * 消费消息响应
 *
 * @author chenchuxin
 * @date 2021/8/24
 */
@Data
public class PullMsgResponse {
    private List<ResponseMsgInfo> messages;
}
