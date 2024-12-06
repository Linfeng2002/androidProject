package com.liu.mall.controller;

import com.liu.mall.api.CommonResult;
import com.liu.mall.service.WebSocketService;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping("/Websocket")
public class WebSocketController {

    @Resource
    WebSocketService webSocketService;

    @ApiOperation(value = "广播消息推送")
    @RequestMapping("/sendAll")
    public CommonResult sednAll(@RequestParam("message") String message, @RequestParam("delayTime") Long delayTime){
        return webSocketService.sendAll(message,delayTime);
    }

    @ApiOperation(value = "获取聊天记录")
    @RequestMapping("/getRecords")
    public CommonResult getRecords(@RequestParam("userId") String userId,@RequestParam("toUserId") String toUserId){
        return webSocketService.getRecords(userId,toUserId);
    }


}
