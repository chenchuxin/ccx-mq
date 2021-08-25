package com.ccx.mq.remoting.protocol.codec;

import com.ccx.mq.remoting.protocol.Command;
import com.ccx.mq.remoting.protocol.compress.Compressor;
import com.ccx.mq.remoting.protocol.compress.CompressorFactory;
import com.ccx.mq.remoting.protocol.consts.CommandType;
import com.ccx.mq.remoting.protocol.consts.CommandCode;
import com.ccx.mq.remoting.protocol.serialize.Serializer;
import com.ccx.mq.remoting.protocol.serialize.SerializerFactory;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;

import static com.ccx.mq.remoting.protocol.consts.CommandFrameConst.*;

/**
 * 命令编解码器
 * <pre>
 * 0   1   2       3   4   5   6   7           8   9   10  11  12       13        14  15  16  17  18  19  20  21  22
 * +---+---+-------+---+---+---+---+-----------+---+---+---+---+--------+---------+---+---+---+---+---+---+---+---+
 * | magic |version|  full length  |commandType|     code      |compress|serialize|            requestId          |
 * +---+---+-------+---+---+---+---+-----------+---+---+---+---+--------+---------+---+---+---+---+---+---+---+---+
 * |                                                                                                              |
 * |                                         body                                                                 |
 * |                                                                                                              |
 * |                                        ... ...                                                               |
 * +--------------------------------------------------------------------------------------------------------------+
 * 2B magic（魔法数）
 * 1B version（版本）
 * 4B full length（消息长度）
 * 1B commandType（命令类型：请求/响应）
 * 4B code (请求标识/响应码)
 * 1B compress（压缩类型）
 * 1B serialize（序列化类型）
 * 8B requestId（请求的Id）
 * body（object类型数据）
 * </pre>
 *
 * @author chenchuxin
 * @date 2021/8/24
 */
public class CommandCodec {

    private static final CommandDecoder DECODER = new CommandDecoder();

    public Command decode(ByteBuf in) {
        return DECODER.decode(in);
    }
}
