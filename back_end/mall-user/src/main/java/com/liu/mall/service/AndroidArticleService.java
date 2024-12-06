package com.liu.mall.service;

import com.liu.mall.model.AndroidArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.mall.vo.Article;
import com.liu.mall.vo.NewArticle;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liu
 * @since 2024-11-21
 */
public interface AndroidArticleService extends IService<AndroidArticle> {

    /**
     * 根据用户喜欢获取推荐文章
     * @param id  用户id
     * @return
     */
    List<Article> getRecommendArticle( int id);

    /**
     * 根据搜索信息获取相关文章
     * @param message 搜索信息
     * @return
     */
    List<Article> getSearchArticle(String message,int id);

    /**
     * 获取文章具体信息
     * @param id 文章id
     * @return
     */
    List<Article> getArticleDetails(List<String> id);

    /**
     * 更新文章信息
     * @param androidArticle
     * @return
     */
    boolean updateArticle(Article androidArticle);


    /**
     * 新增文章信息
     * @param article
     * @return
     */
    boolean insertArticle(NewArticle article);
}
