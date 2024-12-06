package com.liu.mall.mapper;

import com.liu.mall.model.AndroidArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liu
 * @since 2024-12-04
 */
public interface AndroidArticleMapper extends BaseMapper<AndroidArticle> {
//    List<AndroidArticle> getSearchArticle(@Param("message") String message);


    List<AndroidArticle> getRecommendArticle(@Param("logs") List<String> androidLogs);
}
