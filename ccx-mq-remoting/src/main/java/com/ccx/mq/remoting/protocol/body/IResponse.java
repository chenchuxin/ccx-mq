package com.ccx.mq.remoting.protocol.body;

/**
 * 基础响应结果
 *
 * @author chenchuxin
 * @date 2021/8/25
 */
public interface IResponse {

    /**
     * 结果码
     */
    int getCode();

    void setCode(int code);

    /**
     * 备注
     */
    default String getRemark() {
        throw new UnsupportedOperationException("Must implements getRemark method");
    }

    default void setRemark(String remark) {
        throw new UnsupportedOperationException("Must implements setRemark method");
    }
}
