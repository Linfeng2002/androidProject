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
 * @since 2024-12-04
 */
@Getter
@Setter
@TableName("android_article")
@ApiModel(value = "AndroidArticle对象", description = "")
public class AndroidArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("文章作者id")
    private Integer authorId;

    @ApiModelProperty("文章内容")
    private String content;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("作者头像")
    private String authorImage;

    @ApiModelProperty("收藏数")
    private Integer collectNumber;

    @ApiModelProperty("点赞数")
    private Integer likeNumber;

    @ApiModelProperty("文章图片")
    private String picture;

    @ApiModelProperty("文章游览数")
    private Integer visitNum;

    @ApiModelProperty("作者名")
    private String authorUsername;


}
