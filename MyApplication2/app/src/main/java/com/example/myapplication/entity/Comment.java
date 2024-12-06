package com.example.myapplication.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Comment implements Serializable {
    public String id;
    public String commentUserId;
    public String username;
    public String content;
    public String articleId;
    public Timestamp createTime;
    public String userImage;
    public int commentDegree;
    public String parentCommentId;

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(String parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public int getCommentDegree() {
        return commentDegree;
    }

    public void setCommentDegree(int commentDegree) {
        this.commentDegree = commentDegree;
    }

    public Comment() {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreate_time() {
        return createTime;
    }

    public void setCreate_time(Timestamp create_time) {
        this.createTime = create_time;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
