package com.liu.mall.service;

import com.liu.mall.model.AndroidComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liu
 * @since 2024-11-23
 */
public interface AndroidCommentService extends IService<AndroidComment> {

    /**
     * 新增评论
     * @param androidComment
     * @return
     */
    boolean insertComment(AndroidComment androidComment);

    /**
     * 获取评论
     * @param id 所属文章id
     * @return
     */
    List<AndroidComment> getComment(Integer id);

    /**
     * 更新评论
     * @param androidComment
     * @return
     */
    boolean updateComment(AndroidComment androidComment);

    /**
     * 批量插入
     * @param list
     * @return
     */
    boolean insertBatch(List<AndroidComment> list);
}
