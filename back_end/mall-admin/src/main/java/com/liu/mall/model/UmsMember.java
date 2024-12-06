package com.liu.mall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author liu
 * @since 2024-04-25
 */
@Getter
@Setter
@TableName("ums_member")
@ApiModel(value = "UmsMember对象", description = "会员表")
public class UmsMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long memberLevelId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("帐号启用状态:0->禁用；1->启用")
    private Integer status;

    @ApiModelProperty("注册时间")
    private Timestamp createTime;

    @ApiModelProperty("头像")
    private String icon;

    @ApiModelProperty("性别：0->未知；1->男；2->女")
    private Integer gender;

    @ApiModelProperty("生日")
    private Date birthday;

    @ApiModelProperty("所做城市")
    private String city;

    @ApiModelProperty("职业")
    private String job;

    @ApiModelProperty("个性签名")
    private String personalizedSignature;

    @ApiModelProperty("用户来源")
    private Integer sourceType;

    @ApiModelProperty("积分")
    private Integer integration;

    @ApiModelProperty("成长值")
    private Integer growth;

    @ApiModelProperty("剩余抽奖次数")
    private Integer luckeyCount;

    @ApiModelProperty("历史积分数量")
    private Integer historyIntegration;
}
