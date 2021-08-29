package com.ccx.mq.broker.offset;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenchuxin
 * @date 2021/8/29
 */
public class OffsetManager {

    /**
     * 偏移量
     */
    private static final Map<String, Long> offsetMap = new ConcurrentHashMap<>();

    /**
     * 获取 topic 的消费偏移量
     */
    public Long getOffset(String topic) {
        return Optional.ofNullable(offsetMap.get(topic)).orElse(0L);
    }

    /**
     * 更新偏移量
     *
     * @param topic  主题
     * @param offset 更新成此偏移量
     */
    public void updateOffset(String topic, Long offset) {
        offsetMap.put(topic, offset);
    }
}
