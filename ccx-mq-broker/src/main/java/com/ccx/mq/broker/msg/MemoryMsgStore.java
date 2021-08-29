package com.ccx.mq.broker.msg;

import cn.hutool.core.collection.CollectionUtil;
import com.ccx.mq.broker.offset.OffsetManager;
import com.ccx.mq.common.MsgInfo;
import com.ccx.mq.common.SingletonFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
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
@Slf4j
public class MemoryMsgStore implements MsgStore {

    /**
     * 存储。{topic: [StoreMsgInfo]}
     */
    private static final Map<String, List<StoreMsgInfo>> STORE = new ConcurrentHashMap<>();

    /**
     * 写消息的锁
     */
    private static final ReentrantLock PUT_MSG_LOCK = new ReentrantLock();

    /**
     * 偏移量管理类
     */
    private static final OffsetManager OFFSET_MANAGER = SingletonFactory.getSingleton(OffsetManager.class);

    /**
     * 拉取消息
     *
     * @return 获取不到则返回空列表
     */
    @Override
    public List<StoreMsgInfo> pullMessage(String topic, int count) {
        List<StoreMsgInfo> storeMsgInfos = STORE.get(topic);
        if (CollectionUtil.isEmpty(storeMsgInfos)) {
            return Collections.emptyList();
        }
        Long offset = OFFSET_MANAGER.getOffset(topic);
        int start = Math.max(0, getMsgInfoByOffset(offset + 1, storeMsgInfos));
        int end = Math.min(storeMsgInfos.size(), start + count);
        return storeMsgInfos.subList(start, end);
    }

    /**
     * 通过偏移量找到对应的消息索引(对应的offset如果不存在消息，则拿刚好比offset大的最近一条)
     */
    private int getMsgInfoByOffset(long offset, List<StoreMsgInfo> storeMsgInfos) {
        int left = 0;
        int right = storeMsgInfos.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            StoreMsgInfo midMsg = storeMsgInfos.get(mid);
            if (midMsg.getOffset() == offset) {
                return mid;
            }
            if (midMsg.getOffset() < offset) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return right + 1;
    }

    /**
     * 写消息
     *
     * @param msgInfo 消息
     */
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
            long curMaxOffset;
            List<StoreMsgInfo> storeMsgInfos = STORE.get(topic);
            if (storeMsgInfos.size() == 0) {
                curMaxOffset = 0;
            } else {
                StoreMsgInfo lastMsg = storeMsgInfos.get(storeMsgInfos.size() - 1);
                curMaxOffset = lastMsg.getOffset();
            }

            long offset = curMaxOffset + 1;
            StoreMsgInfo newStoreMsgInfo = new StoreMsgInfo();
            newStoreMsgInfo.setOffset(offset);
            newStoreMsgInfo.setTopic(msgInfo.getTopic());
            newStoreMsgInfo.setMsg(msgInfo.getMsg());
            storeMsgInfos.add(newStoreMsgInfo);
            return PutMsgResult.builder().putMsgStatus(PutMsgStatus.SUCCESS).offset(offset).build();
        } catch (Exception exception) {
            log.error("Put msg error. msgInfo: {}", msgInfo, exception);
            return PutMsgResult.builder().putMsgStatus(PutMsgStatus.FAIL).build();
        } finally {
            PUT_MSG_LOCK.unlock();
        }
    }
}
