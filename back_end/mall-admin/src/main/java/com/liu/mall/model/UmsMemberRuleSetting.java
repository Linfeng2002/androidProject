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
 * 会员积分成长规则表
 * </p>
 *
 * @author liu
 * @since 2024-04-25
 */
@Getter
@Setter
@TableName("ums_member_rule_setting")
@ApiModel(value = "UmsMemberRuleSetting对象", description = "会员积分成长规则表")
public class UmsMemberRuleSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("连续签到天数")
    private Integer continueSignDay;

    @ApiModelProperty("连续签到赠送数量")
    private Integer continueSignPoint;

    @ApiModelProperty("每消费多少元获取1个点")
    private BigDecimal consumePerPoint;

    @ApiModelProperty("最低获取点数的订单金额")
    private BigDecimal lowOrderAmount;

    @ApiModelProperty("每笔订单最高获取点数")
    private Integer maxPointPerOrder;

    @ApiModelProperty("类型：0->积分规则；1->成长值规则")
    private Integer type;
}
