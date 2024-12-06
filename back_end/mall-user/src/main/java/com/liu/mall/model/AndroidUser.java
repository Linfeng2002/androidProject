package com.liu.mall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
 * @since 2024-11-25
 */
@Getter
@Setter
@TableName("android_user")
@ApiModel(value = "AndroidUser对象", description = "")
public class AndroidUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("会员经验")
    private Integer memberExperience;

    @ApiModelProperty("收藏的文章id")
    private String collectArticleId;

    @ApiModelProperty("游览的文章id")
    private String visitArticleId;

    @ApiModelProperty("优惠券id")
    private String discountCouponId;

    @ApiModelProperty("用户订单id")
    private String orderId;

    @ApiModelProperty("用户文章·id")
    private String articleId;

    @ApiModelProperty("关注用户id")
    private String followers;

    @ApiModelProperty("用户点赞数")
    private Integer likeNumber;

    @ApiModelProperty("用户粉丝id")
    private String fans;

    @ApiModelProperty("用户头像")
    private String userPicture;

    @ApiModelProperty("点赞文章id")
    private String likeArticleId;
}
