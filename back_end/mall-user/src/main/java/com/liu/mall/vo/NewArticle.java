package com.liu.mall.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Data
public class NewArticle {
    private String articleTitle;
    private String articleContent;
    private Integer authorId;
    private String authorUsername;
    private List<MultipartFile> articlePicture;

}
