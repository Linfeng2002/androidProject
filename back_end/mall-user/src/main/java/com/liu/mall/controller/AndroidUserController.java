package com.liu.mall.controller;

import com.liu.mall.api.CommonResult;
import com.liu.mall.mapper.AndroidUserMapper;
import com.liu.mall.model.AndroidUser;
import com.liu.mall.service.AndroidUserService;
import com.liu.mall.vo.PersonalInformation;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liu
 * @since 2024-11-21
 */
@RestController
@RequestMapping("/androidUser")
public class AndroidUserController {

    @Resource
    AndroidUserService androidUserService;
    @Value("${jwt.tokenHead}")
    private String TOKEN_HEAD;
    @Resource
    AndroidUserMapper androidUserMapper;
    @ApiOperation(value = "请求后返回验证码")
    @RequestMapping(value = "/getAuthCode",method = RequestMethod.GET)
    public CommonResult authCode(@NotEmpty @RequestParam("username") String userPhone){
        String authCode=androidUserService.authCode(userPhone);
        return authCode.isEmpty()?CommonResult.failed():CommonResult.success(authCode);
    }

    @ApiOperation(value = "登录后返回token")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public CommonResult login(@NotEmpty @RequestParam("username")String username,@NotEmpty @RequestParam("password")String password,@NotEmpty @RequestParam("authCode")String authCode){
        String token=androidUserService.login(username,password,authCode);
        if(token==null) return CommonResult.failed();
        AndroidUser userPersonal=androidUserService.getByUsername(username);
        Map<String,String> tokenMap=new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",TOKEN_HEAD);
        tokenMap.put("id",userPersonal.getId().toString());
        tokenMap.put("username",userPersonal.getUsername());
        return CommonResult.success(tokenMap);
    }

    @ApiOperation(value = "获取登录用户信息")
    @RequestMapping(value = "/getByUsername",method = RequestMethod.GET)
    public CommonResult getUserById(@NotEmpty @RequestParam("username") String username){
        PersonalInformation androidUser = androidUserService.getUser(username);
        return androidUser!=null?CommonResult.success(androidUser):CommonResult.failed();
    }

    @ApiOperation(value = "更新个人信息")
    @RequestMapping(value = "/updatePersonal",method = RequestMethod.POST)
    public CommonResult updatePersonal(@NotEmpty @RequestBody PersonalInformation androidUser){
        return  androidUserService.updateUser(androidUser)?CommonResult.success(true):CommonResult.failed();
    }

    @ApiOperation(value = "根据用户id获取信息")
    @RequestMapping(value = "/getById",method = RequestMethod.GET)
    public CommonResult getById(@RequestParam("userId") String id){
        PersonalInformation userById = androidUserService.getUserById(id);
        return userById!=null?CommonResult.success(userById):CommonResult.failed();
    }


}
