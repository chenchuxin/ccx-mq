package com.ccx.mq.broker.topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 主体信息
 *
 * @author chenchuxin
 * @date 2021/8/27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicInfo {
    /**
     * 主题
     */
    private String topic;

    /**
     * 重建时间
     */
    private Date createTime;
}
