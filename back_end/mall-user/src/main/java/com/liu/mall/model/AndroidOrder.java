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
@TableName("android_order")
@ApiModel(value = "AndroidOrder对象", description = "")
public class AndroidOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("订单用户id")
    private Integer orderUserId;

    @ApiModelProperty("订单创建时间")
    private Timestamp orderCreateTime;

    @ApiModelProperty("订单商品数量")
    private Integer productNumber;

    @ApiModelProperty("订单商品单价")
    private Object productPrice;

    @ApiModelProperty("订单商品id")
    private Integer productId;
}
