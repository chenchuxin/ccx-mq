package com.ccx.mq.remoting.consts;

import com.ccx.mq.remoting.protocol.body.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 请求码
 *
 * @author chenchuxin
 * @date 2021/8/25
 */
@Getter
@AllArgsConstructor
public enum CommandCode {
    /**
     * 发消息
     */
    SEND_MSG(1, SendMsgRequest.class, SendMsgResponse.class),

    /**
     * 拉取消息
     */
    PULL_MSG(2, PullMsgRequest.class, PullMsgResponse.class),

    /**
     * 更新偏移量
     */
    UPDATE_OFFSET(3, UpdateOffsetRequest.class, UpdateOffsetResponse.class);

    /**
     * 编码
     */
    private final int code;

    /**
     * 请求 的 class 类型
     */
    private final Class<?> requestClass;

    /**
     * 响应 的 class 类型
     */
    private final Class<?> responseClass;

    public static CommandCode fromCode(int value) {
        for (CommandCode commandCode : CommandCode.values()) {
            if (commandCode.getCode() == value) {
                return commandCode;
            }
        }
        return null;
    }
}
