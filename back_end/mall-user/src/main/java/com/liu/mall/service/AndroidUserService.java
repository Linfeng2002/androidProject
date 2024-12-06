package com.liu.mall.service;

import com.liu.mall.model.AndroidUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.mall.vo.PersonalInformation;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liu
 * @since 2024-11-21
 */
public interface AndroidUserService extends IService<AndroidUser> {

    /**
     * 获取验证码
     * @param userPhone
     * @return
     */
    String authCode(String userPhone);

    /**
     * 根据用户名和密码以及验证码登录
     * @param username
     * @param password
     * @param authCode
     * @return
     */
    String login(String username, String password, String authCode);

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    AndroidUser getByUsername(String username);

    UserDetails loadUserByUsername(String username);

    boolean verifyAuthCode(String userPhone, String authCode);

    /**
     * 根据id查找用户信息，包括收藏和游览历史
     * @param id
     * @return
     */
    PersonalInformation getUserById(String id);

    /**
     *  收藏，游览记录等更新个人信息
     * @param androidUser
     * @return
     */
    boolean updateUser(PersonalInformation androidUser);

    /**
     * 注册用户
     * @param username
     * @param password
     * @return
     */
    boolean register(String username,String password);

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    PersonalInformation getUser(String username);
}
