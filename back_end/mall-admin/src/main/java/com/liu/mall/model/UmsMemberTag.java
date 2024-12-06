package com.liu.mall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户标签表
 * </p>
 *
 * @author liu
 * @since 2024-04-25
 */
@Getter
@Setter
@TableName("ums_member_tag")
@ApiModel(value = "UmsMemberTag对象", description = "用户标签表")
public class UmsMemberTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    @ApiModelProperty("自动打标签完成订单数量")
    private Integer finishOrderCount;

    @ApiModelProperty("自动打标签完成订单金额")
    private BigDecimal finishOrderAmount;
}
