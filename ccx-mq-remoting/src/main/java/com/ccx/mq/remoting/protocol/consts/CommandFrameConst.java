package com.ccx.mq.remoting.protocol.consts;

import cn.hutool.core.util.ByteUtil;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 命令协议框架常量
 *
 * @author chenchuxin
 * @date 2021/8/24
 */
public interface CommandFrameConst {
    /**
     * 魔法数字
     */
    byte[] MAGIC = ByteUtil.numberToBytes((short) 0xff20);

    /**
     * 版本
     */
    byte VERSION = 1;

    /**
     * 请求 Id
     */
    AtomicLong REQUEST_ID = new AtomicLong(0);

    /**
     * 魔法数字长度
     */
    int MAGIC_LENGTH = 2;

    /**
     * 版本长度
     */
    int VERSION_LENGTH = 1;

    /**
     * 总长度字段的长度
     */
    int FULL_LENGTH_LENGTH = 4;

    /**
     * 命令类型长度
     */
    int COMMAND_TYPE_LENGTH = 1;

    /**
     * 返回码长度
     */
    int CODE_LENGTH = 4;

    /**
     * 压缩器类型长度
     */
    int COMPRESS_LENGTH = 1;

    /**
     * 序列化类型长度
     */
    int SERIALIZE_LENGTH = 1;

    /**
     * 请求id 长度
     */
    int REQUEST_ID_LENGTH = 8;

    /**
     * 请求头长度
     */
    int HEADER_LENGTH = MAGIC_LENGTH + VERSION_LENGTH + FULL_LENGTH_LENGTH + COMMAND_TYPE_LENGTH + CODE_LENGTH
            + COMPRESS_LENGTH + SERIALIZE_LENGTH + REQUEST_ID_LENGTH;

    /**
     * 协议最大长度
     */
    int MAX_FRAME_LENGTH = 8 * 1024 * 1024;
}
