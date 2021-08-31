package com.ccx.mq.remoting.protocol.codec;

import com.ccx.mq.remoting.protocol.Command;
import com.ccx.mq.remoting.protocol.compress.Compressor;
import com.ccx.mq.remoting.protocol.compress.CompressorFactory;
import com.ccx.mq.remoting.protocol.consts.CommandFrameConst;
import com.ccx.mq.remoting.protocol.consts.CommandType;
import com.ccx.mq.remoting.protocol.serialize.Serializer;
import com.ccx.mq.remoting.protocol.serialize.SerializerFactory;
import io.netty.buffer.ByteBuf;

import java.util.Optional;

import static com.ccx.mq.remoting.protocol.consts.CommandFrameConst.*;

/**
 * 命令解码器
 *
 * @author chenchuxin
 * @date 2021/8/25
 */
public class CommandEncoder {

    public static final CommandEncoder INSTANT = new CommandEncoder();

    private CommandEncoder() {
    }

    /**
     * 编码，并写到输出流
     *
     * @param cmd 命令
     * @param out 输出流
     */
    public void encode(Command cmd, ByteBuf out) {
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

        // 2B magic code（魔法数）
        out.writeBytes(MAGIC);
        // 1B version（版本）
        out.writeByte(VERSION);
        // 4B full length（消息长度）. 总长度先空着，后面填。
        out.writerIndex(out.writerIndex() + FULL_LENGTH_LENGTH);
        // 1B commandType（消息类型）
        out.writeByte(cmd.getCommandType());
        // 4B commandCode (请求标识/响应码)
        out.writeInt(cmd.getCommandCode());
        // 1B compress（压缩类型）
        out.writeByte(cmd.getCompressorType());
        // 1B serialize（序列化类型）
        out.writeByte(cmd.getSerializerType());
        // 8B requestId（请求的Id）, 心跳不需要，因为服务端不管的
        long requestId = cmd.getCommandType() == CommandType.HEARTBEAT.getValue() ? 0L : REQUEST_ID.getAndIncrement();
        out.writeLong(requestId);

        // 写 body，返回 body 长度
        int bodyLength = writeBody(cmd, out);

        // 当前写指针
        int writerIndex = out.writerIndex();
        out.writerIndex(MAGIC_LENGTH + VERSION_LENGTH);
        // 4B full length（消息长度）
        out.writeInt(HEADER_LENGTH + bodyLength);
        // 写指针复原
        out.writerIndex(writerIndex);
    }

    /**
     * 写 body
     *
     * @return body 长度
     */
    private int writeBody(Command cmd, ByteBuf out) {
        if (cmd.getBody() == null) {
            return 0;
        }

        // 序列化器
        Serializer serializer = SerializerFactory.getSerializer(cmd.getSerializerType());
        if (serializer == null) {
            throw new IllegalArgumentException("unknown serializer type:" + cmd.getSerializerType());
        }

        // 序列化
        byte[] serializeBytes = serializer.serialize(cmd.getBody());

        // 压缩器
        Compressor compressor = CompressorFactory.getCompressor(cmd.getCompressorType());
        byte[] compressedBytes;
        if (compressor != null) {
            // 压缩
            compressedBytes = compressor.compress(serializeBytes);
        } else {
            // 没有压缩器就不压缩了
            compressedBytes = serializeBytes;
        }

        // 写 body
        out.writeBytes(compressedBytes);
        return compressedBytes.length;
    }
}
