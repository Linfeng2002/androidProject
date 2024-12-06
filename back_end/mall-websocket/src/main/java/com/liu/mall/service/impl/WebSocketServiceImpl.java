package com.liu.mall.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.liu.mall.api.CommonResult;
import com.liu.mall.component.WebSocket;
import com.liu.mall.service.WebSocketService;
import com.liu.mall.model.ChatRecords;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WebSocketServiceImpl implements WebSocketService {
    @Resource
    private WebSocket webSocket;


    @Override
    public CommonResult getRecords(String userId, String toUserId) {
        if(StringUtils.isNoneBlank(userId) &&StringUtils.isNoneBlank(toUserId)){
            List<Object> chatRecord1 = webSocketCacheService().getChatRecord(userId, toUserId);
            List<Object> chatRecord2 = webSocketCacheService().getChatRecord(toUserId, userId);
            List<ChatRecords> chatRecords = Stream.concat(chatRecord1.stream(), chatRecord2.stream())//合并两个消息记录
                    .map(entity -> JSONUtil.toBean(JSONUtil.parseObj(entity), ChatRecords.class))//将List<Object>装为List<ChatRecords>>
                    .sorted(Comparator.comparing(ChatRecords::getTime))//升序排序
                    .collect(Collectors.toList());
            return CommonResult.success(chatRecords);
        }
        return null;
    }

    @Override
    public CommonResult sendAll(String message, Long date) {
        if(date!=0){//延时广播
            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
            scheduledExecutorService.schedule(()->{
                webSocket.sendAllMessage(message );
            },date, TimeUnit.MINUTES);
        }
        else webSocket.sendAllMessage(message );
        return CommonResult.success("执行成功");
    }

    public WebSocketCacheServiceImpl webSocketCacheService(){
        return SpringUtil.getBean(WebSocketCacheServiceImpl.class);
    }
}
