package com.liu.mall.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liu.mall.model.AndroidComment;
import com.liu.mall.mapper.AndroidCommentMapper;
import com.liu.mall.service.AndroidCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liu
 * @since 2024-11-23
 */
@Service
public class AndroidCommentServiceImpl extends ServiceImpl<AndroidCommentMapper, AndroidComment> implements AndroidCommentService {

    @Resource
    AndroidCommentMapper androidCommentMapper;
    @Override
    public boolean insertComment(AndroidComment androidComment) {
        //判断评论属于回复文章还是回复文章下的评论
        if(androidComment.getParentCommentId()>0) androidComment.setCommentDegree(2);//如果有父评论，设置级别为2
        else androidComment.setCommentDegree(1);//否则为1级评论
        return androidCommentMapper.insert(androidComment)>0;
    }

    @Override
    public List<AndroidComment> getComment(Integer id) {
        //先获取所有属于该文章的评论，按照评论等级排序
        return androidCommentMapper.selectList(Wrappers.<AndroidComment>lambdaQuery().eq(AndroidComment::getArticleId,id).orderByAsc(AndroidComment::getCommentDegree));
    }

    @Override
    public boolean updateComment(AndroidComment androidComment) {
        return androidCommentMapper.updateById(androidComment)>0;
    }

    @Override
    public boolean insertBatch(List<AndroidComment> list) {
        for (AndroidComment androidComment : list) {
            if(androidComment.getParentCommentId()>0) androidComment.setCommentDegree(2);//如果有父评论，设置级别为2
            else androidComment.setCommentDegree(1);//否则为1级评论
        }
        androidCommentMapper.insertBatch(list);
        return true;
    }
}
