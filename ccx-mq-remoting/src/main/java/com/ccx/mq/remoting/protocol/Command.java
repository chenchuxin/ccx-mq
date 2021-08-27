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
    private Byte version;

    /**
     * 命令类型
     */
    private Byte commandType;

    /**
     * 命令码，具体的命令编码
     */
    private Integer commandCode;

    /**
     * 序列化器类型
     */
    private Byte serializerType;

    /**
     * 压缩器类型
     */
    private Byte compressorType;

    /**
     * 请求id
     */
    private Long requestId;

    /**
     * 命令数据体
     */
    private Object body;
}
