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
@TableName("android_comment")
@ApiModel(value = "AndroidComment对象", description = "")
public class AndroidComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("文章id")
    private Integer articleId;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("创建时间")
    private Timestamp createTime;

    @ApiModelProperty("点赞数")
    private Integer likeNumber;

    @ApiModelProperty("评论等级")
    private Integer commentDegree;

    @ApiModelProperty("父评论id")
    private Integer parentCommentId;

    @ApiModelProperty("评论用户id")
    private Integer commentUserId;

    @ApiModelProperty("评论用户名")
    private String username;

    @ApiModelProperty("评论用户头像")
    private String userImage;
}
