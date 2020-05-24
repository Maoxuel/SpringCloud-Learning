package com.mxl.springcloud.controller;

import com.mxl.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
public class OrderHystrixController {

    @Resource
    private PaymentHystrixService  paymentHystrixService;

    @GetMapping(value = "/consumer/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id){
        String result = paymentHystrixService.paymentInfo_OK(id);
        return result;
    }

    @GetMapping(value = "/consumer/payment/hystrix/timeout/{id}")
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeoutHandler",commandProperties = {
            //设置超时时间的峰值，峰值内可以正常运行，超过了需要有兜底方法处理，作为服务降级fallback
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
    })
    public String paymentInfo_Timeout(@PathVariable("id") Integer id){
//        int age = 10/0;
        String result = paymentHystrixService.paymentInfo_Timeout(id);
        return result;
    }

    public String paymentInfo_TimeoutHandler(Integer id){
        return "线程池： "+Thread.currentThread().getName()+" 支付系统繁忙，请稍后重试,id: "+id;
    }

    //全局的fallback
    public String payment_Global_FallbackMethod(){
        return "global fallback method";
    }
}
