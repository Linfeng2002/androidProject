package com.liu.mall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.sql.Timestamp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author liu
 * @since 2024-11-24
 */
@Getter
@Setter
@TableName("android_log")
@ApiModel(value = "AndroidLog对象", description = "")
public class AndroidLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("搜索日志记录")
    private String message;

    @ApiModelProperty("日志用户")
    private Integer logUesr;

    @ApiModelProperty("日志创建时间")
    private Timestamp createTime;
}
