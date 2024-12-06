package com.liu.mall.component;



import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liu.mall.service.impl.WebSocketCacheServiceImpl;
import com.liu.mall.service.impl.WebSocketServiceImpl;
import com.liu.mall.model.ChatRecords;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ServerEndpoint("/websocket/{userId}")  // 接口路径 ws://localhost:8080/webSocket/userId;

public class WebSocket {

    /**静态变量，用来记录当前在线连接数*/
    private static final AtomicInteger onlineCount = new AtomicInteger(0);
    /**concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。*/
    private static final ConcurrentHashMap<String, WebSocket> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId;

    private List<ChatRecords> chatRecords=new LinkedList<>();
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            // 加入map中
            webSocketMap.put(userId, this);
        } else {
            // 加入map中
            webSocketMap.put(userId, this);
            // 在线数加1
            onlineCount.incrementAndGet();
        }
        log.info("用户连接:" + userId + ",当前在线人数为:" + onlineCount);

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            // 在线人数减1
            onlineCount.decrementAndGet();
            //关闭之后保存聊天记录到redis中
            for (ChatRecords chats:chatRecords){
                webSocketCacheService().setChatRecord(chats);
            }
        }
        log.info("用户退出:" + userId + ",当前在线人数为:" + onlineCount);
    }

    /**
     * 收到客户端消息后调用的方法
     **/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息:" + userId + ",报文:" + message);
        // 解析发送的报文
        JSONObject jsonObject = JSON.parseObject(message);
        // 获取需要转发的用户id
        String toUserId = jsonObject.getString("toUserId");
        String text=jsonObject.getString("message");
        // 传送给对应toUserId用户的websocket
        if (StringUtils.isNotBlank(toUserId) && webSocketMap.containsKey(toUserId)) {
            webSocketMap.get(toUserId).sendMessage(text);
            ChatRecords chatRecords = new ChatRecords();
            chatRecords.setUserId(userId);
            chatRecords.setToUserId(toUserId);
            chatRecords.setTime(new Date(System.currentTimeMillis()));
            chatRecords.setMessage(text);
            this.chatRecords.add(chatRecords);
        } else {
            log.error("对方:" + toUserId + "未连接");
        }
    }


    /**
     * 发生异常调用方法
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器广播推送
     */
    public void sendAllMessage(String message) {
        log.info("【websocket消息】广播消息:" + message);
        for (Map.Entry<String, WebSocket> webSocket : webSocketMap.entrySet()) {
            try {
                if (webSocket.getValue().session.isOpen()) {
                    webSocket.getValue().session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }
    /**
     *发送自定义消息
     **/
    public static void sendInfo(String message, String userId) {
        log.info("发送消息到:" + userId + "，报文:" + message);
        if (StringUtils.isNotBlank(userId) && webSocketMap.containsKey(userId)) {
            webSocketMap.get(userId).sendMessage(message);
        } else {
            log.error("用户" + userId + ",不在线！");
        }
    }

    public WebSocketServiceImpl WebSocketServiceImpl(){
        return SpringUtil.getBean(WebSocketServiceImpl.class);
    }

    public WebSocketCacheServiceImpl webSocketCacheService(){
        return  SpringUtil.getBean(WebSocketCacheServiceImpl.class);
    }
}


