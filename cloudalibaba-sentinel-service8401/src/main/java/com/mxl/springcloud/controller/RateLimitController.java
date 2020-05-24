package com.mxl.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.mxl.springcloud.entities.CommonResult;
import com.mxl.springcloud.entities.Payment;
import com.mxl.springcloud.myHandler.CustomerBlockHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RateLimitController {

    @GetMapping("/byResource")
    @SentinelResource(value = "byResource",blockHandler = "handleException")
    public CommonResult byResource(){
        return new CommonResult(200,"按资源名称限流测试"
                ,new Payment(2020L,"serial001"));
    }

    public CommonResult handleException(BlockException exception){
        return new CommonResult(400,exception.getClass().getCanonicalName()+"服务不可用");
    }

    @GetMapping("/byUrl")
    @SentinelResource(value = "byUrl")
    public CommonResult byUrl(){
        return new CommonResult(200,"按url名称限流测试"
                ,new Payment(2020L,"serial002"));
    }
    @GetMapping("/customerBlockHandler")
    @SentinelResource(value = "customerBlockHandler",
            blockHandlerClass = CustomerBlockHandler.class,
            blockHandler = "handleException2")
    public CommonResult customerBlockHandler(){
        return new CommonResult(200,"按自定义的全局blockHandler限流测试"
                ,new Payment(2020L,"serial002"));
    }
}
