package com.mxl.springcloud.service.impl;

import com.mxl.springcloud.dao.OrderDao;
import com.mxl.springcloud.domain.Order;
import com.mxl.springcloud.service.AccountService;
import com.mxl.springcloud.service.OrderService;
import com.mxl.springcloud.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService
{
    @Resource
    private OrderDao orderDao;
    @Resource
    private AccountService accountService;
    @Resource
    private StorageService storageService;

    @Override
    @GlobalTransactional(name = "fsp-create-order",rollbackFor = Exception.class)
    public void create(Order order) {
        log.info("##############################新建订单");
        orderDao.create(order);

        log.info("#############订单微服务开始调库存做扣减");
        storageService.decrease(order.getProductId(),order.getCount());
        log.info("#############订单微服务开始调库存做扣减结束");

        log.info("#############订单微服务开始调账户做扣减");
        accountService.decrease(order.getUserId(),order.getMoney());
        log.info("#############订单微服务开始调账户做扣减结束");

        //修改订单状态
        log.info("#######修改订单状态");
        orderDao.update(order.getUserId(),0);
        log.info("#######修改订单状态结束");

        log.info("#######此订单结束");

    }
}
