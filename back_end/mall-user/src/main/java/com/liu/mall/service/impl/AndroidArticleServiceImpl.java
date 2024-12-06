package com.liu.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liu.mall.mapper.AndroidCommentMapper;
import com.liu.mall.mapper.AndroidLogMapper;
import com.liu.mall.mapper.AndroidUserMapper;
import com.liu.mall.model.AndroidArticle;
import com.liu.mall.mapper.AndroidArticleMapper;
import com.liu.mall.model.AndroidComment;
import com.liu.mall.model.AndroidLog;
import com.liu.mall.model.AndroidUser;
import com.liu.mall.security.util.SpringUtil;
import com.liu.mall.service.AndroidArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.mall.service.AndroidCommentService;
import com.liu.mall.utils.OSSClientHelper;
import com.liu.mall.vo.Article;
import com.liu.mall.vo.NewArticle;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liu
 * @since 2024-11-21
 */
@Service
public class AndroidArticleServiceImpl extends ServiceImpl<AndroidArticleMapper, AndroidArticle> implements AndroidArticleService {

    @Resource
    AndroidArticleMapper androidArticleMapper;
    @Resource
    AndroidLogMapper androidLogMapper;
    @Resource
    AndroidCommentMapper androidCommentMapper;
    @Resource
    AndroidCommentService androidCommentService;
    @Resource
    AndroidUserMapper androidUserMapper;
    @Resource
    OSSClientHelper ossClientHelper;
    @Override
    public List<Article> getRecommendArticle( int id) {
        List<String> androidLogs=new ArrayList<>();
        List<Article> list=new ArrayList<>();
        if(id>0){
            LambdaQueryWrapper<AndroidLog> androidLogLambdaQueryWrapper = Wrappers.<AndroidLog>lambdaQuery().select(AndroidLog::getMessage).eq(AndroidLog::getLogUesr, id).orderByDesc(AndroidLog::getCreateTime).last("LIMIT 1");
            List<AndroidLog> logs = androidLogMapper.selectList(androidLogLambdaQueryWrapper);
            for (AndroidLog androidLog : logs) {
                androidLogs.add(androidLog.getMessage());
            }
        }if(androidLogs.size()==0) androidLogs.add("");
        //先通过id查搜索记录，如果没有则随机获取文章返回，仅需要前20条文章
        LambdaQueryWrapper<AndroidArticle> last = Wrappers.<AndroidArticle>lambdaQuery().like(AndroidArticle::getContent, androidLogs.get(0)).last("LIMIT 15");
        List<AndroidArticle> androidArticles =androidArticleMapper.selectList(last);
        return getArticles(androidArticles, list);
    }

    @Override
    public List<Article> getSearchArticle(String message,int id) {
        //使用数据库中的like，进行模糊匹配
        LambdaQueryWrapper<AndroidArticle> last = Wrappers.<AndroidArticle>lambdaQuery().like(AndroidArticle::getContent, message).last("LIMIT 15");

        List<AndroidArticle> list=androidArticleMapper.selectList(last);
        if(id>-1){//有id则存入日志
            AndroidLog androidLog = new AndroidLog();
            androidLog.setMessage(message);
            androidLog.setLogUesr(id);
            androidLogMapper.insert(androidLog);//将搜索记录存储起来
        }
        List<Article> articleList=new ArrayList<>();
        return getArticles(list, articleList);
    }

    /**
     * 获取AndroidCommentService防止循环依赖
     * @return
     */
    public AndroidCommentService getAndroidCommentService(){
        return SpringUtil.getBean(AndroidCommentService.class);
    }
    /**
     * AndroidArticle和Article的相互转化
     * @param list
     * @param articleList
     * @return
     */
    private List<Article> getArticles(List<AndroidArticle> list, List<Article> articleList) {
        for (AndroidArticle androidArticle : list) {
            Article article = new Article();
            article.setAuthorName(androidArticle.getAuthorUsername());
            article.setAuthorId(String.valueOf(androidArticle.getAuthorId()));
            article.setCollectNumber(androidArticle.getCollectNumber());
            article.setAuthorImage(androidArticle.getAuthorImage());
            article.setContent(androidArticle.getContent());
            article.setTitle(androidArticle.getTitle());
            article.setVisitNum(androidArticle.getVisitNum());
            article.setLikeNum(androidArticle.getLikeNumber());
            article.setPicture(Arrays.asList(androidArticle.getPicture().split(",")));
            article.setComments(getAndroidCommentService().getComment(androidArticle.getId()));
            article.setId(String.valueOf(androidArticle.getId()));
            articleList.add(article);
        }
        return articleList;
    }

    @Override
    public List<Article> getArticleDetails(List<String> id) {
        List<AndroidArticle> list = androidArticleMapper.selectBatchIds(id);
        List<Article> articles=new ArrayList<>();
        //将所有文章的评论都查找出来,并且以归属文章id和创建时间排序，
        List<AndroidComment> commentList=androidCommentMapper.getCommentByArticleId(id);
        for (AndroidArticle androidArticle : list) {
            Article article = new Article();
            article.authorName= androidArticle.getAuthorUsername();
            article.authorImage=androidArticle.getAuthorImage();
            article.collectNumber=androidArticle.getCollectNumber();
            article.Id= String.valueOf(androidArticle.getId());
            article.authorId= String.valueOf(androidArticle.getAuthorId());
            article.content=androidArticle.getContent();
            article.likeNum=androidArticle.getLikeNumber();
            article.picture= Arrays.asList(androidArticle.getPicture().split(","));
            article.title=androidArticle.getTitle();
            article.visitNum=androidArticle.getVisitNum();
            article.comments=commentList.stream().filter(androidComment -> Objects.equals(androidComment.getArticleId(), androidArticle.getId())).collect(Collectors.toList());
            articles.add(article);
        }
        return articles ;
    }

    @Override
    public boolean updateArticle(Article androidArticle) {
        AndroidArticle localArticle = androidArticleMapper.selectById(androidArticle.Id);
        localArticle.setCollectNumber(androidArticle.getCollectNumber());
        localArticle.setLikeNumber(androidArticle.getLikeNum());
        localArticle.setVisitNum(androidArticle.getVisitNum());
        getAndroidCommentService().insertBatch(androidArticle.getComments());

        return  true;
    }

    @Override
    public boolean insertArticle(NewArticle article) {
        AndroidArticle androidArticle = new AndroidArticle();
        androidArticle.setAuthorUsername(article.getAuthorUsername());

        AndroidUser androidUsers = androidUserMapper.selectOne(Wrappers.<AndroidUser>lambdaQuery().select(AndroidUser::getUserPicture).eq(AndroidUser::getId, article.getAuthorId()));
        androidArticle.setAuthorImage(androidUsers.getUserPicture());
        androidArticle.setTitle(article.getArticleTitle());
        androidArticle.setContent(article.getArticleContent());
        androidArticle.setAuthorId(article.getAuthorId());

        StringBuilder stringBuilder = new StringBuilder();
        for (MultipartFile inputStream : article.getArticlePicture()) {
            //将输入流上传到oss
            try {
                stringBuilder.append(ossClientHelper.upload(inputStream.getInputStream(),article.getAuthorId())).append(",") ;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);//把最后一个逗号删掉
        androidArticle.setPicture(stringBuilder.toString());
        androidArticleMapper.insert(androidArticle);
        return false;
    }


}
