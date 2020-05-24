package com.mxl.springcloud.dao;

import com.mxl.springcloud.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderDao {

    //新建订单
    void create(Order order);

    void update(@Param("userId") Long userId,@Param("status") Integer status);
}
