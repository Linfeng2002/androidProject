package com.liu.mall.controller;

import com.liu.mall.api.CommonResult;
import com.liu.mall.model.UmsAdmin;
import com.liu.mall.model.UmsAdminProgram;
import com.liu.mall.model.UmsResource;
import com.liu.mall.model.UmsRole;
import com.liu.mall.service.Impl.UmsAdminServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台用户表 前端控制器
 * </p>
 *
 * @author liu
 * @since 2024-04-25
 */
@RestController
@RequestMapping("admin")
public class UmsAdminController {

    @Value("Bearer")
    private String tokenHead;

    @Autowired
    private UmsAdminServiceImpl umsAdminService;

    @ApiOperation(value = "登录成功以后返回token")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public  CommonResult login(@Validated  @RequestBody UmsAdminProgram umsAdminProgram){
        String token= umsAdminService.login(umsAdminProgram.getUsername(),umsAdminProgram.getPassword());
        if(token==null){
            return CommonResult.failed("用户名或密码错误");
        }
        HashMap<String, String> tokeMap = new HashMap<String, String>();
        tokeMap.put("token",token);
        tokeMap.put("tokenHead" ,tokenHead);
        return CommonResult.success(tokeMap);
    }

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public CommonResult register(@Validated @RequestBody UmsAdminProgram umsAdminProgram){
        UmsAdmin umsAdmin=umsAdminService.register(umsAdminProgram);
        if(umsAdmin==null) return CommonResult.failed();
        return CommonResult.success(umsAdmin);
    }


    @ApiOperation(value = "刷新token")
    @RequestMapping(value = "/refresh",method = RequestMethod.POST)
    public CommonResult refresh(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader(tokenHead);
        String refreshToken=umsAdminService.refreshToken(token);
        if(refreshToken==null) return  CommonResult.failed("token已过期");

        Map<String,String> tokenMap= new HashMap<>();
        tokenMap.put("tokne",refreshToken);
        tokenMap.put("tokenHead",tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation(value = "获取指定用户的角色")
    @RequestMapping(value = "/role/{adminId}",method = RequestMethod.GET)
    public CommonResult<List<UmsRole>> getRoleList(@PathVariable Long adminId){
        List<UmsRole> roleList=umsAdminService.getRoleList(adminId);
        return roleList==null?CommonResult.failed():CommonResult.success(roleList);
    }

    @ApiOperation(value = "获取指定用户信息")
    @RequestMapping(value = "/getItem/{id}",method = RequestMethod.GET)
    public CommonResult<UmsAdmin> getItem(@PathVariable Long id){
        UmsAdmin item = umsAdminService.getItem(id);
        return item==null?CommonResult.failed():CommonResult.success(item);
    }

    @ApiOperation(value = "测试")
    @RequestMapping(value = "/test",method = RequestMethod.POST)
    public CommonResult<List<UmsResource>> test(@RequestBody UmsAdmin umsAdmin){
         List<UmsResource> list=umsAdminService.test(umsAdmin);
        return list==null?CommonResult.failed():CommonResult.success(list);
    }
}
