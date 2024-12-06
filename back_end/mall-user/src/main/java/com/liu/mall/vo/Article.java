package com.liu.mall.vo;

import com.liu.mall.model.AndroidComment;
import lombok.Data;

import java.util.List;

@Data
public class Article {
    public String Id;
    public String authorName;
    public String authorId;
    public String title;
    public List<String> picture;
    public int visitNum;
    public int likeNum;
    public String authorImage;
    public String content;
    public int collectNumber;
    public List<AndroidComment> comments;
}
