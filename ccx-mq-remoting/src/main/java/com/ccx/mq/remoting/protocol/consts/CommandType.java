package com.ccx.mq.remoting.protocol.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 命令类型
 *
 * @author chenchuxin
 * @date 2021/8/25
 */
@Getter
@AllArgsConstructor
public enum CommandType {
    REQUEST((byte) 1),
    RESPONSE((byte) 2);
    private final byte value;

    public static CommandType fromValue(byte value) {
        for (CommandType commandType : CommandType.values()) {
            if (commandType.getValue() == value) {
                return commandType;
            }
        }
        return null;
    }
}