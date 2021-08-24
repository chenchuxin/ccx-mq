package com.ccx.mq.remoting.protocol;

import com.ccx.mq.remoting.protocol.compress.Compressor;
import com.ccx.mq.remoting.protocol.compress.CompressorFactory;
import com.ccx.mq.remoting.protocol.consts.CommandType;
import com.ccx.mq.remoting.protocol.consts.RequestCode;
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

    /**
     * 解码
     *
     * @param in 字节流
     * @return 命令对象
     */
    public Command decode(ByteBuf in) {
        readAndCheckMagic(in);
        byte version = in.readByte();
        int fullLength = in.readInt();
        byte commandType = in.readByte();
        int code = in.readInt();
        byte compressorType = in.readByte();
        byte serializerType = in.readByte();
        long requestId = in.readLong();
        Command cmd = Command.builder().version(version)
                .commandType(commandType).code(code)
                .compressorType(compressorType)
                .serializerType(serializerType)
                .requestId(requestId)
                .build();
        Object body = decodeBody(in, fullLength, cmd);
        cmd.setBody(body);
        return cmd;
    }

    /**
     * 解码消息体
     *
     * @param in         字节流
     * @param fullLength 总长度
     * @param cmd        除了 body 之外，其他都赋值的 Command 类
     * @return 消息体，如果获取不到，返回 null
     */
    private Object decodeBody(ByteBuf in, int fullLength, Command cmd) {
        int bodyLength = fullLength - HEADER_LENGTH;
        if (bodyLength == 0) {
            return null;
        }

        byte[] bodyBytes = new byte[bodyLength];
        in.readBytes(bodyBytes);

        // 解压
        Compressor compressor = CompressorFactory.getCompressor(cmd.getCompressorType());
        if (compressor == null) {
            throw new IllegalArgumentException("unknown compress type:" + cmd.getCompressorType());
        }
        byte[] decompressedBytes = compressor.decompress(bodyBytes);

        // 反序列化
        Serializer serializer = SerializerFactory.getSerializer(cmd.getSerializerType());
        if (serializer == null) {
            throw new IllegalArgumentException("unknown serializer type:" + cmd.getSerializerType());
        }

        Class<?> clazz = null;
        if (cmd.getCommandType() == CommandType.REQUEST.getValue()) {
            RequestCode requestCode = RequestCode.fromCode(cmd.getCommandType());
            if (requestCode == null) {
                throw new IllegalArgumentException("unknown command type:" + cmd.getCommandType());
            }
            clazz = requestCode.getBodyClass();
        }
        // TODO: CommandType.RESPONSE
        return serializer.deserialize(decompressedBytes, clazz);
    }

    /**
     * 读取并检查魔数
     */
    private void readAndCheckMagic(ByteBuf in) {
        byte[] bytes = new byte[MAGIC_LENGTH];
        in.readBytes(bytes);
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != MAGIC[i]) {
                throw new IllegalArgumentException("Unknown magic: " + Arrays.toString(bytes));
            }
        }
    }
}
