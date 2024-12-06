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
 * 积分变化历史记录表
 * </p>
 *
 * @author liu
 * @since 2024-04-25
 */
@Getter
@Setter
@TableName("ums_integration_change_history")
@ApiModel(value = "UmsIntegrationChangeHistory对象", description = "积分变化历史记录表")
public class UmsIntegrationChangeHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long memberId;

    private Timestamp createTime;

    @ApiModelProperty("改变类型：0->增加；1->减少")
    private Integer changeType;

    @ApiModelProperty("积分改变数量")
    private Integer changeCount;

    @ApiModelProperty("操作人员")
    private String operateMan;

    @ApiModelProperty("操作备注")
    private String operateNote;

    @ApiModelProperty("积分来源：0->购物；1->管理员修改")
    private Integer sourceType;
}
