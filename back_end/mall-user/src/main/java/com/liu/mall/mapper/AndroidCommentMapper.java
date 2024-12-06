package com.liu.mall.mapper;

import com.liu.mall.model.AndroidComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liu
 * @since 2024-11-24
 */
public interface AndroidCommentMapper extends BaseMapper<AndroidComment> {

    List<AndroidComment> getCommentByArticleId(@Param("ids") List<String> id);

    void insertBatch(@Param("comments") List<AndroidComment> comments);
}
