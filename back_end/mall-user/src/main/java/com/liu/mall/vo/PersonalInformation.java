package com.liu.mall.vo;

import com.liu.mall.model.AndroidArticle;
import com.liu.mall.model.AndroidOrder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PersonalInformation  {
    public int userId;
    public String username;
    public String userPicture;
    public List<Integer> likeArticleId;
    public List<Article> attentionArticle;//收藏历史
    public List<Article> recordArticle;//游览历史
    public List<AndroidOrder> userAndroidOrder;//用户订单
    public int likeNumber;
    public String[] follower;//关注用户id
    public String[] fans;//粉丝id
    public List<Article> userArticle;//用户个人文章

}
