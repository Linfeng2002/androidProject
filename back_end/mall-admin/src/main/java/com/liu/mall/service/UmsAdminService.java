package com.liu.mall.service;

import com.liu.mall.model.UmsAdmin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.mall.model.UmsAdminProgram;
import com.liu.mall.model.UmsResource;
import com.liu.mall.model.UmsRole;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author liu
 * @since 2024-04-25
 */
public interface UmsAdminService extends IService<UmsAdmin> {

    /**
     *登录以返回token
     * @param username 用户名
     * @param password  密码
     * @return token字符串
     */
    String login(String username,String password);

    /**
     *根据用户名加载用户信息
     * @param username 用户名
     * @return 用户信息
     */
    UserDetails loadUserByUsername(String username);

    /**
     *获取缓存服务
     */
    UmsAdminCacheService umsAdminCacheService();

    /**
     * 通过id获取用户资源
     * @param i id
     */
    List<UmsResource> getUmsResourceById(Long i);

    /**
     * 根据id查找用户信息
     * @param adminId
     * @return
     */
    UmsAdmin getItem(Long adminId);

    /**
     * 注册新用户
     * @param umsAdminProgram
     * @return
     */
    UmsAdmin register(UmsAdminProgram umsAdminProgram);

    /**
     * 刷新token时间
     * @param token
     * @return
     */
    String refreshToken(String token);

    /**
     * 获取用户对应角色
     * @param adminId
     * @return
     */
    List<UmsRole> getRoleList(Long adminId);

    /**
     * 测试
     * @param umsAdmin
     * @return
     */
    List<UmsResource> test(UmsAdmin umsAdmin);

}
