package com.ccx.mq.remoting.protocol.body;

import lombok.Data;

/**
 * 通用响应结果
 *
 * @author chenchuxin
 * @date 2021/8/25
 */
@Data
public class Result {

    /**
     * 结果码
     */
    private int code;

    /**
     * 备注
     */
    private String remark;

    /**
     * 响应数据
     */
    private Object response;
}
