package com.mxl.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
public class PaymentService {

    //正常情况
    public String paymentInfo_OK(Integer id){
        return "线程池： "+Thread.currentThread().getName()+" paymentInfo_OK,id: "+id;
    }

    //访问超时
//    @HystrixCommand(fallbackMethod = "paymentInfo_TimeoutHandler",commandProperties = {
//            //设置超时时间的峰值，峰值内可以正常运行，超过了需要有兜底方法处理，作为服务降级fallback
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
//    })
    @HystrixCommand
    public String paymentInfo_Timeout(Integer id){
//        int age = 10/0;
        int timeNumber = 5;
        try {
            TimeUnit.SECONDS.sleep(timeNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池： "+Thread.currentThread().getName()+" paymentInfo_Timeout,id: "+id+" 耗时(s)： "+timeNumber;
    }

    public String paymentInfo_TimeoutHandler(Integer id){
        return "线程池： "+Thread.currentThread().getName()+" 系统繁忙，请稍后重试,id: "+id;
    }

    //全局的fallback
    public String payment_Global_FallbackMethod(){
        return "global fallback method";
    }

    //服务熔断**********************
    //在10s的窗口期内，如果10次请求中有60%的请求失败，则跳闸,默认是10s，20次，50%
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),//是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),//请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),//时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"),//失败率达到多少后跳闸
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id){
        if (id<0){
            throw new RuntimeException("id不能为负数");
        }
        String uuid = IdUtil.simpleUUID();
        return Thread.currentThread().getName()+"\t"+"调用成功，流水号是："+uuid;
    }
    public String paymentCircuitBreaker_fallback(@PathVariable("id")Integer id){
        return "id不能是负数，此时id="+id;
    }
}
