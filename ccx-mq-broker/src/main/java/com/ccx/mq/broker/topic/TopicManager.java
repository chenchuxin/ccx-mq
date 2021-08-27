package com.ccx.mq.broker.topic;

import cn.hutool.core.util.StrUtil;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * topic 管理
 *
 * @author chenchuxin
 * @date 2021/8/27
 */
public class TopicManager {

    private static final Map<String, TopicInfo> topicMap = new ConcurrentHashMap<>();

    public static final TopicManager INSTANT = new TopicManager();

    private TopicManager() {
    }

    /**
     * 如果需要，就创建
     */
    public void createIfNeed(String topic) {
        if (!topicMap.containsKey(topic)) {
            TopicInfo topicInfo = TopicInfo.builder().topic(topic).createTime(new Date()).build();
            topicMap.putIfAbsent(topic, topicInfo);
        }
    }
}
