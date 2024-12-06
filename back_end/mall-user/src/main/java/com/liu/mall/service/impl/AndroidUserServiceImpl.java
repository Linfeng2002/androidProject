package com.liu.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liu.mall.exception.ApiException;
import com.liu.mall.mapper.AndroidArticleMapper;
import com.liu.mall.mapper.AndroidOrderMapper;
import com.liu.mall.model.*;
import com.liu.mall.mapper.AndroidUserMapper;
import com.liu.mall.security.util.JwtTokenUtil;
import com.liu.mall.service.AndroidArticleService;
import com.liu.mall.service.AndroidUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.mall.service.RedisService;
import com.liu.mall.vo.Article;
import com.liu.mall.vo.PersonalInformation;
import org.aspectj.weaver.ast.And;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liu
 * @since 2024-11-21
 */
@Service
public class AndroidUserServiceImpl extends ServiceImpl<AndroidUserMapper, AndroidUser> implements AndroidUserService {

    @Resource
    AndroidUserMapper androidUserMapper;
    @Resource
    RedisService redisService;
    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${redis.key.expire.authCode}")
    private Long REDIS_KEY_EXPIRE_CODE;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.key.user}")
    private String REDIS_KEY_USER;
    @Resource
    PasswordEncoder passwordEncoder;
    @Resource
    JwtTokenUtil jwtTokenUtil;
    @Resource
    AndroidOrderMapper androidOrderMapper;
    @Resource
    AndroidArticleMapper androidArticleMapper;
    @Resource
    AndroidArticleService androidArticleService;
    @Override
    public String authCode(String userPhone) {

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE + userPhone, sb, REDIS_KEY_EXPIRE_CODE);
        return sb.toString();
    }

    @Override
    public AndroidUserDetails loadUserByUsername(String username) {
        AndroidUser androidUser = getByUsername(username);
        if (androidUser != null) {
            return new AndroidUserDetails(androidUser);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    @Override
    public AndroidUser getByUsername(String username) {
        AndroidUser androidUser = (AndroidUser) redisService.get(REDIS_DATABASE + ":" + REDIS_KEY_USER + ":" + username);
        if (androidUser != null) {
            return androidUser;
        }
        LambdaQueryWrapper<AndroidUser> androidUserLambdaQueryWrapper = Wrappers.<AndroidUser>lambdaQuery().eq(AndroidUser::getUsername, username);
        androidUser = androidUserMapper.selectOne(androidUserLambdaQueryWrapper);

        return androidUser;
    }

    @Override
    public String login(String username, String password, String authCode) {
        String token = null;
        if (verifyAuthCode(username, authCode)) {
            AndroidUser androidUser = getByUsername(username);
            if(androidUser==null){//等于空则跳转到注册
                if(register(username,password)) androidUser=getByUsername(username);
            }
            AndroidUserDetails userDetails = new AndroidUserDetails(androidUser);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new ApiException("密码不正确");
            }//密码正确时将用户信息存入redis
            redisService.set(REDIS_DATABASE + ":" + REDIS_KEY_USER + ":" + username, userDetails.getUserPersonal(), REDIS_KEY_EXPIRE_CODE);
            token = jwtTokenUtil.generateToken(userDetails);
        }

        return token;
    }

    @Override
    public boolean verifyAuthCode (String userPhone, String authCode){
        String realAuthCode = (String) redisService.get(REDIS_KEY_PREFIX_AUTH_CODE + userPhone);
        if (realAuthCode.equals(authCode)) {
            return true;
        }
        return false;
    }

    @Override
    public PersonalInformation getUserById(String id) {
        AndroidUser androidUser = androidUserMapper.selectById(id);
        PersonalInformation personalInformation = new PersonalInformation();
        personalInformation.setFans(androidUser.getFans().split(","));
        personalInformation.setFollower(androidUser.getFollowers().split(","));
        personalInformation.setUserId(androidUser.getId());
        personalInformation.setLikeNumber(androidUser.getLikeNumber());
        personalInformation.setUsername(androidUser.getUsername());
        personalInformation.setUserPicture(androidUser.getUserPicture());
        //设置用户订单
        List<String> list=new ArrayList<>();
//        if(androidUser.getOrderId()!=null){
//             String[]temp = androidUser.getOrderId().split(",");
//            list.addAll(Arrays.asList(temp));
//            personalInformation.setUserAndroidOrder(androidOrderMapper.selectBatchIds(list));
//            list.clear();
//        }

        //防止返回结果报空，设置一个默认值
        List<Integer> arrayList = new ArrayList<>();
        if(androidUser.getLikeArticleId()!=null){
            String[] split = androidUser.getLikeArticleId().split(",");
            if(!split[0].equals("")){
                for (String s : split) {
                    arrayList.add(Integer.parseInt(s));
                }
            }

        }
        personalInformation.setLikeArticleId(arrayList);




        //设置收藏历史

        if(androidUser.getCollectArticleId()!=null){
            String[]temp= androidUser.getCollectArticleId().split(",");
            list.addAll(Arrays.asList(temp));
            personalInformation.setAttentionArticle( androidArticleService.getArticleDetails(list));
            list.clear();
        }else personalInformation.setAttentionArticle( new ArrayList<>());


        //设置游览历史

        if (androidUser.getVisitArticleId() != null) {
            String[]temp = androidUser.getVisitArticleId().split(",");
            list.addAll(Arrays.asList(temp));
            personalInformation.setRecordArticle(androidArticleService.getArticleDetails(list));
            list.clear();
        }else personalInformation.setRecordArticle(new ArrayList<>());


        //设置用户文章
        if (androidUser.getArticleId() != null) {
            String[]temp =androidUser.getArticleId().split(",");
            list.addAll(Arrays.asList(temp));
            personalInformation.setUserArticle( androidArticleService.getArticleDetails(list));
        }else personalInformation.setUserArticle(new ArrayList<>());

        return personalInformation;
    }

    @Override
    public boolean updateUser(PersonalInformation personalInformation) {
        AndroidUser androidUser = androidUserMapper.selectById(personalInformation.userId);
        androidUser.setFans(Arrays.toString(personalInformation.getFans()));
        androidUser.setFollowers(Arrays.toString(personalInformation.getFollower()));
        androidUser.setLikeNumber(personalInformation.getLikeNumber());
        androidUser.setUsername(personalInformation.getUsername());
        androidUser.setUserPicture(personalInformation.getUserPicture());
        StringBuilder stringBuilder = new StringBuilder();
        for (AndroidOrder androidOrder : personalInformation.getUserAndroidOrder()) {
            stringBuilder.append(androidOrder.getId());
        }
        androidUser.setOrderId(String.valueOf(stringBuilder));//设置用户订单id
        stringBuilder.delete(0,stringBuilder.length());
        for (Article androidArticle : personalInformation.getAttentionArticle()) {
            stringBuilder.append(androidArticle.getId());
        }
        androidUser.setCollectArticleId(String.valueOf(stringBuilder));//设置收藏文章id
        stringBuilder.delete(0,stringBuilder.length());
        for (Article androidArticle : personalInformation.getUserArticle()) {
            stringBuilder.append(androidArticle.getId());
        }
        androidUser.setArticleId(String.valueOf(stringBuilder));//设置用户文章id
        stringBuilder.delete(0,stringBuilder.length());
        for (Article androidArticle : personalInformation.getRecordArticle()) {
            stringBuilder.append(androidArticle.getId());
        }
        androidUser.setVisitArticleId(String.valueOf(stringBuilder));//设置游览文章历史
        androidUserMapper.updateById(androidUser);
        return true;
    }

    @Override
    public boolean register(String username, String password) {
        AndroidUser androidUser = new AndroidUser();
        androidUser.setUsername(username);
        androidUser.setPassword(passwordEncoder.encode(password));//存储加密后的数据
        androidUser.setVisitArticleId("");
        androidUser.setArticleId("");
        androidUser.setUserPicture("");
        androidUser.setOrderId("");
        androidUser.setCollectArticleId("");
        androidUser.setFollowers("");
        androidUser.setFans("");
        androidUserMapper.insert(androidUser);
        return true;
    }

    @Override
    public PersonalInformation getUser(String username) {
        LambdaQueryWrapper<AndroidUser> androidUserLambdaQueryWrapper = Wrappers.<AndroidUser>lambdaQuery().eq(AndroidUser::getUsername, username);
        AndroidUser androidUser = androidUserMapper.selectOne(androidUserLambdaQueryWrapper);
        return getUserById(String.valueOf(androidUser.getId()));
    }
}