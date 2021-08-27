package com.ccx.mq.remoting.protocol.body;

/**
 * 最基础的响应结果
 *
 * @author chenchuxin
 * @date 2021/8/27
 */
public abstract class BaseResponse implements IResponse {

    /**
     * 必须要有无参构造函数
     */
    public BaseResponse() {
    }

    private int code;

    private String remark;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
