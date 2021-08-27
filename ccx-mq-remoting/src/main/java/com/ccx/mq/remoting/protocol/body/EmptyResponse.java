package com.ccx.mq.remoting.protocol.body;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 空响应，适用于没有响应字段的命令
 *
 * @author chenchuxin
 * @date 2021/8/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmptyResponse extends BaseResponse {
}
