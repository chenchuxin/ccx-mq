package com.ccx.mq.demo;

import cn.hutool.core.net.NetUtil;
import com.ccx.mq.client.producer.Producer;
import com.ccx.mq.remoting.protocol.Command;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * 生产者控制器
 *
 * @author chenchuxin
 * @date 2021/8/29
 */
@RestController
@RequestMapping("producer")
public class ProducerController {

    private Producer producer;

    @PostConstruct
    public void init() {
        producer = new Producer(NetUtil.getLocalHostName(), 4624);
        producer.start();
    }

    /**
     * 发消息
     */
    @GetMapping("send")
    public Command send(String topic, String msg) {
        if (msg == null) {
            msg = "{id: 12345, name: \"小明\"}";
        }
        if (topic == null) {
            topic = "my_topic";
        }
        return producer.sendMsg(topic, msg);
    }
}
