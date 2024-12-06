package com.liu.mall.service;

import com.liu.mall.model.UmsAdmin;
import com.liu.mall.model.UmsResource;


import java.util.List;

/**
 * 后台用户缓存管理Service
 * Created by macro on 2020/3/13.
 */
public interface UmsAdminCacheService {
    /**
     * 删除后台用户缓存
     */
    void delAdmin(Long adminId);

    /**
     * 删除后台用户资源列表缓存
     */
    void delResourceList(Long adminId);



    /**
     * 获取缓存后台用户信息
     */
    UmsAdmin getAdmin(String username);

    /**
     * 设置缓存后台用户信息
     */
    void setAdmin(UmsAdmin admin);

    /**
     * 获取缓存后台用户资源列表
     */
    List<UmsResource> getResourceList(Long adminId);

    /**
     * 设置缓存后台用户资源列表
     */
    void setResourceList(Long adminId, List<UmsResource> resourceList);
}
