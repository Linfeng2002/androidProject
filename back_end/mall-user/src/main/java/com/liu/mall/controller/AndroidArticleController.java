package com.liu.mall.controller;

import com.google.gson.Gson;
import com.liu.mall.api.CommonResult;
import com.liu.mall.model.AndroidArticle;
import com.liu.mall.service.AndroidArticleService;
import com.liu.mall.vo.Article;
import com.liu.mall.vo.NewArticle;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liu
 * @since 2024-11-21
 */
@RestController
@RequestMapping("/androidArticle")
public class AndroidArticleController {

    @Resource
    AndroidArticleService androidArticleService;

    @ApiOperation(value = "获取推荐文章信息")
    @RequestMapping(value = "/getRecommendArticle",method = RequestMethod.GET)
    public CommonResult  getRecommendArticle(@RequestParam("userId") int id){
        List<Article> list=androidArticleService.getRecommendArticle(id);
        return list!=null?CommonResult.success(list):CommonResult.failed();
    }

    @ApiOperation(value = "获取搜索相关文章")
    @RequestMapping(value = "/getSearchArticle",method = RequestMethod.GET)
    public CommonResult getSearchArticle(@RequestParam("searchMessage")@NotEmpty String message, @RequestParam("userId") int id){
        List<Article> list=androidArticleService.getSearchArticle(message,id);
        return list!=null?CommonResult.success(list):CommonResult.failed();
    }

    @ApiOperation(value = "根据文章id获取具体信息")
    @RequestMapping(value = "/getArticleDetails",method = RequestMethod.GET)
    public CommonResult getArticleDetails(@RequestParam("articleId") List<String> id ){
        List<Article> articleDetails = androidArticleService.getArticleDetails(id);
        return articleDetails!=null?CommonResult.success(articleDetails):CommonResult.failed();
    }

    @ApiOperation(value = "更新文章相关信息")
    @RequestMapping(value = "/updateArticle",method = RequestMethod.POST)
    public CommonResult updateArticle(@RequestBody Article androidArticle){
        return androidArticleService.updateArticle(androidArticle)?CommonResult.success(true):CommonResult.failed();
    }

    @ApiOperation(value = "插入新文章")
    @RequestMapping(value = "/insertArticle",method = RequestMethod.POST)
    public CommonResult insertArticle(@RequestPart("article") String articleJson,
                                      @RequestPart("images") List<MultipartFile> files){
        Gson gson = new Gson();
        NewArticle article = gson.fromJson(articleJson, NewArticle.class);
        article.setArticlePicture(files);
        return androidArticleService.insertArticle(article)?CommonResult.success(true):CommonResult.failed();
    }

}
