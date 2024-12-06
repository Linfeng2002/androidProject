package com.liu.mall.mapper;

import com.liu.mall.model.UmsRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 Mapper 接口
 * </p>
 *
 * @author liu
 * @since 2024-04-25
 */
public interface UmsRoleMapper extends BaseMapper<UmsRole> {

    List<UmsRole> getRoleByAdminId (@Param("id") Long id);
}
