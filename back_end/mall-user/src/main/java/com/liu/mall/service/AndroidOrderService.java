package com.liu.mall.service;

import com.liu.mall.model.AndroidOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liu
 * @since 2024-11-21
 */
public interface AndroidOrderService extends IService<AndroidOrder> {

    /**
     * 获取订单信息
     * @param id
     * @return
     */
    List<AndroidOrder> getOrder(List<Integer> id);

    /**
     * 新增订单信息
     * @param androidOrder 订单列表
     */
    boolean insertOrder(List<AndroidOrder> androidOrder);
}
