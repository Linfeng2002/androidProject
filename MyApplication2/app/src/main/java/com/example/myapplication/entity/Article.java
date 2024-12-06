package com.example.myapplication.entity;

import com.example.myapplication.R;
import com.example.myapplication.utils.ImageUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Article implements Serializable {
    private static final long serialVersionUID = 1L;
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
    public List<Comment> comments;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Article(String id, String authorName, String authorId, String title, List<String> picture, int visitNum, int likeNum, String authorImage, String content, int collectNumber, List<Comment> comments) {
        Id = id;
        this.authorName = authorName;
        this.authorId = authorId;
        this.title = title;
        this.picture = picture;
        this.visitNum = visitNum;
        this.likeNum = likeNum;
        this.authorImage = authorImage;
        this.content = content;
        this.collectNumber = collectNumber;
        this.comments = comments;
    }

    public static String[] authorArray={"tom","chris","john","kun"};

    public static String[] titleArray={"广州是个宜居的城市","深圳是个赚钱的城市","珠海是个美丽的城市","佛山是美食之城"};

    public static String[] pictureArray={"/storage/emulated/0/Android/data/com.example.myapplication/files/Download/1733192845171.jpeg"};

    public Article() {

    }


    public static List<Article> getDefault(){
        ArrayList<Article> list = new ArrayList<>();
        for(int i=0;i<authorArray.length;i++){
            ArrayList<String> list1 = new ArrayList<>();
            list1.add(pictureArray[0]);
            list.add(new Article("0","chris",authorArray[i],titleArray[i],list1,0,0 ,"/storage/emulated/0/Android/data/com.example.myapplication/files/Download/ic_launcher_author-playstore.png","",1,new ArrayList<>()));
        }
        return list;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAuthor() {
        return authorName;
    }

    public void setAuthor(String author) {
        this.authorName = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getPicture() {
        return picture;
    }

    public void setPicture(List<String> picture) {
        this.picture = picture;
    }

    public int getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(int visitNum) {
        this.visitNum = visitNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public String getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCollectNumber() {
        return collectNumber;
    }

    public void setCollectNumber(int collectNumber) {
        this.collectNumber = collectNumber;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public static String[] getAuthorArray() {
        return authorArray;
    }

    public static void setAuthorArray(String[] authorArray) {
        Article.authorArray = authorArray;
    }

    public static String[] getTitleArray() {
        return titleArray;
    }

    public static void setTitleArray(String[] titleArray) {
        Article.titleArray = titleArray;
    }

    public static String[] getPictureArray() {
        return pictureArray;
    }

    public static void setPictureArray(String[] pictureArray) {
        Article.pictureArray = pictureArray;
    }
}
