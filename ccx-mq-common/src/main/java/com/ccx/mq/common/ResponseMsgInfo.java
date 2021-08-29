package com.ccx.mq.common;

import lombok.Data;

/**
 * 消息
 *
 * @author chenchuxin
 * @date 2021/8/27
 */
@Data
public class ResponseMsgInfo {
    private String msg;
    private Long offset;
}
