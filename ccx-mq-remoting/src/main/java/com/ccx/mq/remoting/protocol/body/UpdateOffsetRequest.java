package com.ccx.mq.remoting.protocol.body;

import lombok.Data;

/**
 * 更新偏移量请求
 *
 * @author chenchuxin
 * @date 2021/8/24
 */
@Data
public class UpdateOffsetRequest {

    private String topic;

    /**
     * 要更新的偏移量
     */
    private long offset;
}
