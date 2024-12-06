package com.liu.mall.service.impl;

import com.liu.mall.exception.ApiException;
import com.liu.mall.model.AndroidOrder;
import com.liu.mall.mapper.AndroidOrderMapper;
import com.liu.mall.service.AndroidOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liu
 * @since 2024-11-21
 */
@Service
public class AndroidOrderServiceImpl extends ServiceImpl<AndroidOrderMapper, AndroidOrder> implements AndroidOrderService {

    @Resource
    AndroidOrderMapper androidOrderMapper;
    @Override
    public List<AndroidOrder> getOrder(List<Integer> id) {
        List<AndroidOrder> list = androidOrderMapper.selectBatchIds(id);
        return list;
    }

    @Override
    public boolean insertOrder(List<AndroidOrder> androidOrder) {
        try {
            this.saveBatch(androidOrder);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
        return true;
    }
}
