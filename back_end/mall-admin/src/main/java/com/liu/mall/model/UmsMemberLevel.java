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
 * 会员等级表
 * </p>
 *
 * @author liu
 * @since 2024-04-25
 */
@Getter
@Setter
@TableName("ums_member_level")
@ApiModel(value = "UmsMemberLevel对象", description = "会员等级表")
public class UmsMemberLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer growthPoint;

    @ApiModelProperty("是否为默认等级：0->不是；1->是")
    private Integer defaultStatus;

    @ApiModelProperty("免运费标准")
    private BigDecimal freeFreightPoint;

    @ApiModelProperty("每次评价获取的成长值")
    private Integer commentGrowthPoint;

    @ApiModelProperty("是否有免邮特权")
    private Integer priviledgeFreeFreight;

    @ApiModelProperty("是否有签到特权")
    private Integer priviledgeSignIn;

    @ApiModelProperty("是否有评论获奖励特权")
    private Integer priviledgeComment;

    @ApiModelProperty("是否有专享活动特权")
    private Integer priviledgePromotion;

    @ApiModelProperty("是否有会员价格特权")
    private Integer priviledgeMemberPrice;

    @ApiModelProperty("是否有生日特权")
    private Integer priviledgeBirthday;

    private String note;
}
