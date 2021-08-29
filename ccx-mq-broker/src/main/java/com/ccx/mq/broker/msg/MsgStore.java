package com.ccx.mq.broker.msg;

import com.ccx.mq.common.MsgInfo;

import java.util.List;

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

    /**
     * 拉消息
     *
     * @param topic 主题
     * @return 获取不到则返回空列表
     */
    List<StoreMsgInfo> pullMessage(String topic, int count);

}
