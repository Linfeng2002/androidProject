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
 * 会员与产品分类关系表（用户喜欢的分类）
 * </p>
 *
 * @author liu
 * @since 2024-04-25
 */
@Getter
@Setter
@TableName("ums_member_product_category_relation")
@ApiModel(value = "UmsMemberProductCategoryRelation对象", description = "会员与产品分类关系表（用户喜欢的分类）")
public class UmsMemberProductCategoryRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long memberId;

    private Long productCategoryId;
}
