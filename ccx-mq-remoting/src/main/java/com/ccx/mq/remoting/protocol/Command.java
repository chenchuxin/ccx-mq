package com.ccx.mq.remoting.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 远程命令
 *
 * @author chenchuxin
 * @date 2021/8/24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Command {

    /**
     * 版本
     */
    private byte version;

    /**
     * 命令类型
     */
    private byte commandType;

    /**
     * 结果码
     */
    private int code;

    /**
     * 序列化器类型
     */
    private byte serializerType;

    /**
     * 压缩器类型
     */
    private byte compressorType;

    /**
     * 请求id
     */
    private long requestId;

    /**
     * 命令数据体
     */
    private Object body;
}
