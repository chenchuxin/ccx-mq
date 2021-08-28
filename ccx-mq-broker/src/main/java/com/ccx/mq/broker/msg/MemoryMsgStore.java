package com.ccx.mq.broker.msg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 内存消息存储
 *
 * @author chenchuxin
 * @date 2021/8/27
 */
public class MemoryMsgStore implements MsgStore {

    /**
     * 存储。{topic: [StoreMsgInfo]}
     */
    private static final Map<String, List<StoreMsgInfo>> STORE = new ConcurrentHashMap<>();

    /**
     * 写消息的锁
     */
    private static final ReentrantLock PUT_MSG_LOCK = new ReentrantLock();

    @Override
    public PutMsgResult putMessage(MsgInfo msgInfo) {
        PUT_MSG_LOCK.lock();
        try {
            String topic = msgInfo.getTopic();
            if (!STORE.containsKey(topic)) {
                STORE.put(topic, new ArrayList<>());
            }

            // 拿到当前最大的offset。
            // 1. 如果当前有数据：从最后一条拿到 offset
            // 2. 如果没数据：当前最大 offset = 0
            int curMaxOffset;
            List<StoreMsgInfo> storeMsgInfos = STORE.get(topic);
            if (storeMsgInfos.size() == 0) {
                curMaxOffset = 0;
            } else {
                StoreMsgInfo lastMsg = storeMsgInfos.get(storeMsgInfos.size() - 1);
                curMaxOffset = lastMsg.getOffset();
            }

            int offset = curMaxOffset + 1;
            StoreMsgInfo newStoreMsgInfo = new StoreMsgInfo();
            newStoreMsgInfo.setOffset(offset);
            newStoreMsgInfo.setTopic(msgInfo.getTopic());
            newStoreMsgInfo.setMsg(msgInfo.getMsg());
            storeMsgInfos.add(newStoreMsgInfo);
        } finally {
            PUT_MSG_LOCK.unlock();
        }
        return PutMsgResult.builder().putMsgStatus(PutMsgStatus.SUCCESS).build();
    }
}
