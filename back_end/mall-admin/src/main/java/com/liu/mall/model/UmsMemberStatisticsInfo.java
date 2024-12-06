package com.liu.mall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 会员统计信息
 * </p>
 *
 * @author liu
 * @since 2024-04-25
 */
@Getter
@Setter
@TableName("ums_member_statistics_info")
@ApiModel(value = "UmsMemberStatisticsInfo对象", description = "会员统计信息")
public class UmsMemberStatisticsInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long memberId;

    @ApiModelProperty("累计消费金额")
    private BigDecimal consumeAmount;

    @ApiModelProperty("订单数量")
    private Integer orderCount;

    @ApiModelProperty("优惠券数量")
    private Integer couponCount;

    @ApiModelProperty("评价数")
    private Integer commentCount;

    @ApiModelProperty("退货数量")
    private Integer returnOrderCount;

    @ApiModelProperty("登录次数")
    private Integer loginCount;

    @ApiModelProperty("关注数量")
    private Integer attendCount;

    @ApiModelProperty("粉丝数量")
    private Integer fansCount;

    private Integer collectProductCount;

    private Integer collectSubjectCount;

    private Integer collectTopicCount;

    private Integer collectCommentCount;

    private Integer inviteFriendCount;

    @ApiModelProperty("最后一次下订单时间")
    private Timestamp recentOrderTime;
}
