package com.ccx.mq.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例工厂
 *
 * @author chenchuxin
 * @date 2021/8/29
 */
public class SingletonFactory {

    private static final Map<Class<?>, Object> objectCache = new ConcurrentHashMap<>();

    private static final Object lock = new Object();

    private SingletonFactory() {
    }

    /**
     * 获取单例对象
     *
     * @param clazz class 对象
     * @param <T>   class 类型
     * @return 对象
     */
    public static <T> T getSingleton(Class<T> clazz) {
        if (!objectCache.containsKey(clazz)) {
            synchronized (lock) {
                if (!objectCache.containsKey(clazz)) {
                    try {
                        objectCache.put(clazz, clazz.newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //noinspection unchecked
        return (T) objectCache.get(clazz);
    }
}
