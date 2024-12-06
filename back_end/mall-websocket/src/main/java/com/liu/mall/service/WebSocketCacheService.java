package com.liu.mall.service;

import com.liu.mall.model.ChatRecords;

import java.util.List;

public interface WebSocketCacheService {

    /**
     * 存储聊天记录
     */
    void setChatRecord(ChatRecords chatRecords);

    /**
     * 获取聊天记录
     * @return
     */
    List<Object> getChatRecord(String userId, String toUserId);
}
