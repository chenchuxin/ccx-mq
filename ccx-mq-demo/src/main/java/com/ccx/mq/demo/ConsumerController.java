package com.ccx.mq.demo;

import cn.hutool.core.net.NetUtil;
import com.ccx.mq.client.consumer.Consumer;
import com.ccx.mq.client.producer.Producer;
import com.ccx.mq.common.ResponseMsgInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 消费者控制器
 *
 * @author chenchuxin
 * @date 2021/8/29
 */
@RestController
@RequestMapping("consumer")
public class ConsumerController {

    private Consumer consumer;

    @PostConstruct
    public void init() {
        consumer = new Consumer(NetUtil.getLocalHostName(), 4624);
        consumer.start();
    }

    /**
     * 拉消息
     */
    @GetMapping("pull")
    public List<ResponseMsgInfo> pull(@RequestParam(defaultValue = "my_topic") String topic,
                                      @RequestParam(defaultValue = "10") Integer count) {
        return consumer.pull(topic, count);
    }
}
