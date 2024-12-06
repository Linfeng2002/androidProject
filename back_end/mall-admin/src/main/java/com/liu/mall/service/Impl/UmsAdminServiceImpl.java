package com.liu.mall.service.Impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.liu.mall.exception.Asserts;
import com.liu.mall.mapper.UmsAdminRoleRelationDao;
import com.liu.mall.mapper.UmsResourceDao;
import com.liu.mall.mapper.UmsRoleMapper;
import com.liu.mall.model.*;
import com.liu.mall.mapper.UmsAdminMapper;
import com.liu.mall.service.UmsAdminCacheService;
import com.liu.mall.service.UmsAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.mall.security.util.JwtTokenUtil;
import com.liu.mall.security.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author liu
 * @since 2024-04-25
 */
@Service
@SuppressWarnings("all")
public class UmsAdminServiceImpl extends ServiceImpl<UmsAdminMapper, UmsAdmin> implements UmsAdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminServiceImpl.class);

    @Autowired
    UmsAdminMapper umsAdminMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    UmsRoleMapper umsRoleMapper;
    @Autowired
    UmsResourceDao umsResourceDao;
    @Autowired
    UmsAdminRoleRelationDao umsAdminRoleRelationDao;
    @Override
    public String login(String username,String password) {
        String token=null;

        try {
            UserDetails userDetails = loadUserByUsername(username);
            if(!passwordEncoder.matches(password,userDetails.getPassword())) Asserts.fail("密码不正确");
            if(!userDetails.isEnabled()) Asserts.fail("账号已禁用");

            //资源权限设置
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            //生成token
            token=jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            LOGGER.warn(e.getMessage());
        }

        return token;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        UmsAdmin umsAdmin=getUserAdminByUsername(username);
        if(umsAdmin !=null){
            List<UmsResource> umsResources=getUmsResourceById(umsAdmin.getId());
            return new AdminUserDetails(umsAdmin,umsResources);
        }
        return null;
    }



    @Override
    public List<UmsResource> getUmsResourceById(Long id) {
        //从redis缓存中获取
        List<UmsResource> umsResourceList=umsAdminCacheService().getResourceList(id);
        if(CollUtil.isNotEmpty(umsResourceList)) return umsResourceList;

        //缓存中没有则查询数据库
        umsResourceList =umsAdminMapper.getAdminResources(id);
        if(CollUtil.isNotEmpty(umsResourceList)){
            umsAdminCacheService().setResourceList(id,umsResourceList);
            return umsResourceList;
        }



        return null;
    }

    @Override
    public UmsAdmin getItem(Long adminId) {
        List<UmsAdmin> umsAdmins = umsAdminMapper.getUsernameById(adminId);
        if(CollUtil.isNotEmpty(umsAdmins)){
            return umsAdmins.get(0);
        }
        return null;
    }

    @Override
    public UmsAdmin register(UmsAdminProgram umsAdminProgram) {
        UmsAdmin umsAdmin = new UmsAdmin();
        umsAdmin.setUsername(umsAdminProgram.getUsername());
        umsAdmin.setCreateTime(new Timestamp(new Date().getTime()));
        //查找是否有相同用户
        LambdaQueryWrapper<UmsAdmin> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(UmsAdmin::getUsername,umsAdminProgram.getUsername());

        List<UmsAdmin> umsAdmin1=umsAdminMapper.selectList(lambdaQueryWrapper);
        if(umsAdmin1.size()>0) return null;

        //若无则加密后新增用户
        umsAdmin.setPassword(passwordEncoder.encode(umsAdminProgram.getPassword()));
        if(umsAdminMapper.insert(umsAdmin)<1) return null;


        return umsAdminMapper.selectOne(lambdaQueryWrapper);

    }

    public UmsAdmin getUserAdminByUsername(String username){
        //从redis缓存中获取
        UmsAdmin umsAdmin=umsAdminCacheService().getAdmin(username);
        if(umsAdmin!=null) return umsAdmin;
        //redis缓存中没有则查询数据库
        LambdaQueryWrapper<UmsAdmin> wrapper=new LambdaQueryWrapper();
        wrapper.eq(UmsAdmin::getUsername,username);
        umsAdmin=umsAdminMapper.selectOne(wrapper);
        if(umsAdmin!=null){
            umsAdminCacheService().setAdmin(umsAdmin);
            return umsAdmin;
        }
        return null;
    }

    @Override
    public String refreshToken(String token) {
        return jwtTokenUtil.refreshHeadToken(token);
    }



    @Override
    public List<UmsRole> getRoleList(Long adminId) {
        return umsRoleMapper.getRoleByAdminId(adminId);
    }

    @Override
    public UmsAdminCacheService umsAdminCacheService() {
        return SpringUtil.getBean(UmsAdminCacheService.class);
    }

    @Override
    public List<UmsResource> test(UmsAdmin umsAdmin) {
        long currentTimeMillis = System.currentTimeMillis();
        long laterCurrentTimeMillis=System.currentTimeMillis();

        MPJLambdaWrapper<UmsResource> wrapper = JoinWrappers.lambda(UmsResource.class);

        wrapper.selectAll(UmsResource.class)
                .leftJoin(UmsRoleResourceRelation.class,UmsRoleResourceRelation::getResourceId,UmsResource::getId)
                .leftJoin(UmsAdminRoleRelation.class,UmsAdminRoleRelation::getRoleId,UmsRoleResourceRelation::getRoleId)
                .eq(UmsAdminRoleRelation::getAdminId,umsAdmin.getId());
        for(int i=0;i<1000;i++){
            umsResourceDao.selectList(wrapper);
        }
        laterCurrentTimeMillis=System.currentTimeMillis();
//        System.out.println(laterCurrentTimeMillis-currentTimeMillis);
        Long time1=laterCurrentTimeMillis-currentTimeMillis;
        currentTimeMillis=System.currentTimeMillis();
        MPJLambdaWrapper<UmsAdminRoleRelation> wrapper1 = JoinWrappers.lambda(UmsAdminRoleRelation.class);

        wrapper1.leftJoin(UmsRoleResourceRelation.class,UmsRoleResourceRelation::getRoleId,UmsAdminRoleRelation::getRoleId)
                .leftJoin(UmsResource.class,UmsResource::getId,UmsRoleResourceRelation::getResourceId)
                .selectAll(UmsResource.class)
                .eq(UmsAdminRoleRelation::getAdminId,umsAdmin.getId());
        for(int i=0;i<1000;i++){
            umsAdminRoleRelationDao.selectList(wrapper1);
        }
        laterCurrentTimeMillis=System.currentTimeMillis();
//        System.out.println(laterCurrentTimeMillis-currentTimeMillis);
        Long time2=laterCurrentTimeMillis-currentTimeMillis;
        int[] answer=new int[10];
        int length = answer.length;
        return umsResourceDao.selectList(wrapper);

    }
}
