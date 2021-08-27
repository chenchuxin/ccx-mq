package com.ccx.mq.remoting.protocol.consts;

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
     * 成功
     */
    SUCCESS(0, null),

    TOPIC_NOT_VALID(1, "Topic 不合法"),
    ;

    /**
     * 响应码
     */
    private final int code;

    /**
     * 备注
     */
    private final String remark;
}
