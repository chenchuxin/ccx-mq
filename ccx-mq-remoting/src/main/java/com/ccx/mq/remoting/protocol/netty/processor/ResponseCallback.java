package com.ccx.mq.remoting.protocol.netty.processor;

import com.ccx.mq.remoting.protocol.Command;

/**
 * 响应回调
 *
 * @author chenchuxin
 * @date 2021/8/27
 */
public interface ResponseCallback {

    /**
     * 回调
     *
     * @param response 响应
     */
    void callback(Command response);
}
