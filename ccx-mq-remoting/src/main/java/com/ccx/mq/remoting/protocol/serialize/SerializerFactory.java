package com.ccx.mq.remoting.protocol.serialize;

import cn.hutool.core.map.MapBuilder;
import com.ccx.mq.remoting.consts.SerializeType;

import java.util.Map;

/**
 * 序列化器工厂
 *
 * @author chenchuxin
 * @date 2021/8/25
 */
public class SerializerFactory {

    private static final Map<SerializeType, Serializer> map = MapBuilder.<SerializeType, Serializer>create()
            .put(SerializeType.PROTOSTUFF, new ProtostuffSerializer())
            .build();

    public static Serializer getSerializer(SerializeType type) {
        return map.get(type);
    }

    public static Serializer getSerializer(byte type) {
        return getSerializer(SerializeType.fromValue(type));
    }
}
