package com.ccx.mq.remoting.protocol.consts;

import com.ccx.mq.remoting.protocol.body.PullMsgRequest;
import com.ccx.mq.remoting.protocol.body.SendMsgRequest;
import com.ccx.mq.remoting.protocol.body.UpdateOffsetRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应码
 *
 * @author chenchuxin
 * @date 2021/8/25
 */
@Getter
@AllArgsConstructor
public enum ResponseCode {
    /**
     * 发消息
     */
    SEND_MSG(1, SendMsgRequest.class),

    /**
     * 拉取消息
     */
    PULL_MSG(2, PullMsgRequest.class),

    /**
     * 更新偏移量
     */
    UPDATE_OFFSET(3, UpdateOffsetRequest.class);

    /**
     * 编码
     */
    private final int code;

    /**
     * body 的 class 类型
     */
    private final Class<?> bodyClass;
}
