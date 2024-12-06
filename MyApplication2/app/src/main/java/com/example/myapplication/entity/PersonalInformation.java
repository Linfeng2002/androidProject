package com.example.myapplication.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.myapplication.R;
import com.example.myapplication.converter.personalConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PersonalInformation implements Serializable  {

    @PrimaryKey
    @NonNull
    public int userId;
    public String username;
    public String userPicture;

    @TypeConverters({personalConverter.class})
    public List<Integer> likeArticleId;
    @TypeConverters({personalConverter.class})
    public List<Article> attentionArticle;//收藏历史
    @TypeConverters({personalConverter.class})
    public List<Article> recordArticle;//游览历史
    @TypeConverters({personalConverter.class})
    public List<Order> userOrder;//用户订单
    public int likeNumber;
    @TypeConverters({personalConverter.class})
    public List<String> follower;
    @TypeConverters({personalConverter.class})
    public List<String> fans;
    @TypeConverters({personalConverter.class})
    public List<Article> userArticle;//用户个人文章

    public PersonalInformation() {
    }

    public List<Integer> getLikeArticleId() {
        return likeArticleId;
    }

    public void setLikeArticleId(List<Integer> likeArticleId) {
        this.likeArticleId = likeArticleId;
    }
    @Ignore
    public PersonalInformation(int userId, String username,  String userPicture, List<Integer> likeArticleId, List<Article> attentionArticle, List<Article> recordArticle, List<Order> userOrder, int likeNumber, List<String> follower, List<String> fans, List<Article> userArticle) {
        this.userId = userId;
        this.username = username;
        this.userPicture = userPicture;
        this.likeArticleId = likeArticleId;
        this.attentionArticle = attentionArticle;
        this.recordArticle = recordArticle;
        this.userOrder = userOrder;
        this.likeNumber = likeNumber;
        this.follower = follower;
        this.fans = fans;
        this.userArticle = userArticle;
    }
    @Ignore
    public static PersonalInformation getDefaultPersonalInformation(){
       return new PersonalInformation(0,"chris", "/storage/self/primary/Android/data/com.example.myapplication/files/Download/ic_launcher_author-playstore.png",new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),0,new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public List<Article> getAttentionArticle() {
        return attentionArticle;
    }

    public void setAttentionArticle(List<Article> attentionArticle) {
        this.attentionArticle = attentionArticle;
    }

    public List<Article> getRecordArticle() {
        return recordArticle;
    }

    public void setRecordArticle(List<Article> recordArticle) {
        this.recordArticle = recordArticle;
    }

    public List<Order> getUserOrder() {
        return userOrder;
    }

    public void setUserOrder(List<Order> userOrder) {
        this.userOrder = userOrder;
    }

    public int getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(int likeNumber) {
        this.likeNumber = likeNumber;
    }

    public List<String> getFollower() {
        return follower;
    }

    public void setFollower(List<String> follower) {
        this.follower = follower;
    }

    public List<String> getFans() {
        return fans;
    }

    public void setFans(List<String> fans) {
        this.fans = fans;
    }

    public List<Article> getUserArticle() {
        return userArticle;
    }

    public void setUserArticle(List<Article> userArticle) {
        this.userArticle = userArticle;
    }
}
