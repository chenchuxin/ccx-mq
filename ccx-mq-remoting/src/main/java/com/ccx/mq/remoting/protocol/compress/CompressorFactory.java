package com.ccx.mq.remoting.protocol.compress;

import cn.hutool.core.map.MapBuilder;
import com.ccx.mq.remoting.protocol.consts.CompressType;

import java.util.Map;

/**
 * 压缩器工厂
 *
 * @author chenchuxin
 * @date 2021/8/25
 */
public class CompressorFactory {

    private static final Map<CompressType, Compressor> map = MapBuilder.<CompressType, Compressor>create()
            .put(CompressType.GZIP, new GzipCompressor())
            .build();

    public static Compressor getCompressor(CompressType type) {
        return map.get(type);
    }

    public static Compressor getCompressor(byte type) {
        return getCompressor(CompressType.fromValue(type));
    }
}
