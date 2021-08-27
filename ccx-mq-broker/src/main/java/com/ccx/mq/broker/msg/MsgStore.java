package com.ccx.mq.broker.msg;

/**
 * 消息存储
 *
 * @author chenchuxin
 * @date 2021/8/27
 */
public interface MsgStore {

    /**
     * 写消息
     *
     * @param msgInfo 消息
     * @return 结果
     */
    PutMsgResult putMessage(MsgInfo msgInfo);
}
