package com.liu.mall.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "ChatRecords对象",description = "聊天记录存储对象")
public class ChatRecords {
    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("用户聊天对象ID")
    private String toUserId;
    @ApiModelProperty("聊天记录时间")
    private Date time;
    @ApiModelProperty("聊天记录")
    private String message;
}
