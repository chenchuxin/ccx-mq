package com.ccx.mq.remoting.protocol.codec;

import com.ccx.mq.remoting.protocol.Command;
import io.netty.buffer.ByteBuf;

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
 * 4B commandCode (命令编码)
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

    public static final CommandCodec INSTANT = new CommandCodec();

    private CommandCodec() {
    }

    /**
     * 编码，并写到输出流
     *
     * @param cmd 命令
     * @param out 输出流
     */
    public void encode(Command cmd, ByteBuf out) {
        CommandEncoder.INSTANT.encode(cmd, out);
    }

    /**
     * 命令解码
     *
     * @param in 字节流
     * @return 命令
     */
    public Command decode(ByteBuf in) {
        return CommandDecoder.INSTANT.decode(in);
    }
}
