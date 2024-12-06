package com.liu.mall.service.impl;

import com.liu.mall.service.RedisService;
import com.liu.mall.service.WebSocketCacheService;

import com.liu.mall.model.ChatRecords;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@SuppressWarnings("all")
public class WebSocketCacheServiceImpl implements WebSocketCacheService {


    @Resource
    RedisService redisService;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;

    @Override
    public void setChatRecord(ChatRecords chatRecords) {
        String key=REDIS_DATABASE+":"+chatRecords.getUserId()+":"+chatRecords.getToUserId();
        redisService.lPush(key,chatRecords,REDIS_EXPIRE);
    }

    @Override
    public List<Object> getChatRecord(String userId, String toUserId) {
        String key=REDIS_DATABASE+":"+userId+":"+toUserId;
        List<Object> chatRecord =redisService.lRange(key,0,redisService.lSize(key)-1);
        return chatRecord;
    }
}
