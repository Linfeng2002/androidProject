package com.liu.mall.controller;

import com.liu.mall.api.CommonResult;
import com.liu.mall.model.AndroidOrder;
import com.liu.mall.service.AndroidOrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liu
 * @since 2024-11-21
 */
@RestController
@RequestMapping("/androidOrder")
public class AndroidOrderController {

    @Resource
    AndroidOrderService androidOrderService;

    @ApiOperation(value = "获取订单信息")
    @RequestMapping(value = "/getOrder",method = RequestMethod.GET)
    public CommonResult getOrder(@RequestParam("orderId") List<Integer> id){
        List<AndroidOrder> list=androidOrderService.getOrder(id);
        return list!=null?CommonResult.success(list):CommonResult.failed();
    }

    @ApiOperation(value = "新增订单")
    @RequestMapping(value = "/insertOrder",method = RequestMethod.POST)
    public CommonResult insertOrder(@RequestBody List<AndroidOrder> androidOrder){
        return androidOrderService.insertOrder(androidOrder) ?CommonResult.success(true):CommonResult.failed();
    }


}
