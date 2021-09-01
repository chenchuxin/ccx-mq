package com.ccx.mq.remoting.protocol.codec;

import com.ccx.mq.remoting.consts.CommandCode;
import com.ccx.mq.remoting.consts.CommandType;
import com.ccx.mq.remoting.protocol.Command;
import com.ccx.mq.remoting.protocol.compress.Compressor;
import com.ccx.mq.remoting.protocol.compress.CompressorFactory;
import com.ccx.mq.remoting.protocol.serialize.Serializer;
import com.ccx.mq.remoting.protocol.serialize.SerializerFactory;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;

import static com.ccx.mq.remoting.consts.CommandFrameConst.*;

/**
 * 命令解码器
 *
 * @author chenchuxin
 * @date 2021/8/25
 */
public class CommandDecoder {

    public static final CommandDecoder INSTANT = new CommandDecoder();

    private CommandDecoder() {
    }

    /**
     * 解码
     *
     * @param in 字节流
     * @return 命令对象
     */
    public Command decode(ByteBuf in) {
        /*
         * 消息格式：
         * 2B magic（魔法数）
         * 1B version（版本）
         * 4B full length（消息长度）
         * 1B commandType（命令类型：请求/响应）
         * 4B commandCode (命令编码)
         * 1B compress（压缩类型）
         * 1B serialize（序列化类型）
         * 8B requestId（请求的Id）
         * body（object类型数据）
         */
        readAndCheckMagic(in);
        byte version = in.readByte();
        int fullLength = in.readInt();
        byte commandType = in.readByte();
        int commandCode = in.readInt();
        byte compressorType = in.readByte();
        byte serializerType = in.readByte();
        long requestId = in.readLong();
        Command cmd = Command.builder()
                .version(version)
                .commandType(commandType)
                .commandCode(commandCode)
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
        byte[] decompressedBytes;
        if (compressor == null) {
            decompressedBytes = bodyBytes;
        } else {
            decompressedBytes = compressor.decompress(bodyBytes);
        }

        // 反序列化
        Serializer serializer = SerializerFactory.getSerializer(cmd.getSerializerType());
        if (serializer == null) {
            throw new IllegalArgumentException("unknown serializer type:" + cmd.getSerializerType());
        }

        CommandCode commandCode = CommandCode.fromCode(cmd.getCommandCode());
        if (commandCode == null) {
            throw new IllegalArgumentException("unknown command type:" + cmd.getCommandType());
        }
        Class<?> clazz = cmd.getCommandType() == CommandType.REQUEST.getValue()
                ? commandCode.getRequestClass()
                : commandCode.getResponseClass();
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
