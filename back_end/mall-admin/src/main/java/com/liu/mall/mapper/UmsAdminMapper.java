package com.liu.mall.mapper;

import com.liu.mall.model.UmsAdmin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liu.mall.model.UmsResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author liu
 * @since 2024-04-25
 */
//@Mapper
public interface UmsAdminMapper extends BaseMapper<UmsAdmin>  {
    List<UmsResource> getAdminResources (@Param("id") Long id);

    List<UmsAdmin> getUsernameById(@Param("id") Long id);
}
