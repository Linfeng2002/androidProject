package com.liu.mall.controller;

import com.liu.mall.api.CommonResult;
import com.liu.mall.model.AndroidComment;
import com.liu.mall.service.AndroidCommentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liu
 * @since 2024-11-23
 */
@RestController
@RequestMapping("/androidComment")
public class AndroidCommentController {

    @Resource
    AndroidCommentService androidCommentService;

    @ApiOperation(value = "新增评论")
    @RequestMapping(value = "/insertComment",method = RequestMethod.POST)
    public CommonResult insertComment(@RequestBody AndroidComment androidComment){
        return androidCommentService.insertComment(androidComment)?CommonResult.success(true):CommonResult.failed();
    }

    @ApiOperation(value = "获取评论")
    @RequestMapping(value = "/getComment",method = RequestMethod.GET)
    public CommonResult getComment(@RequestParam Integer id){
        List<AndroidComment> list=androidCommentService.getComment(id);
        return list!=null?CommonResult.success(list):CommonResult.failed();
    }

    @ApiOperation(value = "更新评论")
    @RequestMapping(value = "updateComment")
    public CommonResult updateComment(@RequestBody AndroidComment androidComment){
        return androidCommentService.updateComment(androidComment)?CommonResult.success(true):CommonResult.failed();
    }

    @ApiOperation(value = "删除评论")
    @RequestMapping(value = "deleteComment")
    public CommonResult deleteComment(@RequestBody AndroidComment androidComment){
        return androidCommentService.removeById(androidComment.getId())?CommonResult.success(true):CommonResult.failed();
    }
}
