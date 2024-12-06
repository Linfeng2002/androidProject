package com.liu.mall.service.Impl;

import com.liu.mall.model.UmsAdminPermissionRelation;
import com.liu.mall.mapper.UmsAdminPermissionRelationMapper;
import com.liu.mall.service.UmsAdminPermissionRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台用户和权限关系表(除角色中定义的权限以外的加减权限) 服务实现类
 * </p>
 *
 * @author liu
 * @since 2024-04-25
 */
@Service
public class UmsAdminPermissionRelationServiceImpl extends ServiceImpl<UmsAdminPermissionRelationMapper, UmsAdminPermissionRelation> implements UmsAdminPermissionRelationService {

}
