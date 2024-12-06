package com.liu.mall.service;

import com.liu.mall.api.CommonResult;



public interface WebSocketService {
    /**
     * 发送广播消息
     * @param message 广播信息
     * @param date  延迟时间
     * @return
     */
    CommonResult sendAll(String message, Long date);

    /**
     * 获取聊天记录
     * @param userId
     * @param toUserId
     * @return
     */
    CommonResult getRecords(String userId, String toUserId);
}
