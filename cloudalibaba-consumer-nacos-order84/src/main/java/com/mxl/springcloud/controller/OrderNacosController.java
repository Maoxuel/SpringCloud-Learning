package com.mxl.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.mxl.springcloud.entities.CommonResult;
import com.mxl.springcloud.entities.Payment;
import com.mxl.springcloud.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
public class OrderNacosController {

    @Resource
    private RestTemplate restTemplate;

    @Value("${service-url.nacos-user-service}")
    private String serviceURL;

    @GetMapping(value = "/consumer/fallback/{id}")
//    @SentinelResource(value = "fallback") //没有配置
//    @SentinelResource(value = "fallback",fallback = "handleFallback") //fallback只负责业务异常
//    @SentinelResource(value = "fallback",blockHandler = "blockHandler") //blockHandler只负责sentinel控制台配置违规
    @SentinelResource(value = "fallback",
            fallback = "handleFallback",
            blockHandler = "blockHandler",
            exceptionsToIgnore = {IllegalArgumentException.class})
    public CommonResult  fallback(@PathVariable("id") Long id){
        CommonResult result = restTemplate.getForObject(serviceURL + "/paymentSQL/" + id, CommonResult.class, id);
        if(id ==4){
            throw new IllegalArgumentException("非法参数异常");
        }else if(result.getData()==null){
            throw new NullPointerException("空指针异常");
        }
        return result;
    }


    //fallback
    public CommonResult handleFallback(@PathVariable("id") Long id,Throwable e){
        Payment payment = new Payment(id,null);
        return new CommonResult<>(400,"fallback handler: "+e.getMessage(),payment);
    }
    //block handler
    public CommonResult blockHandler(@PathVariable("id") Long id, BlockException e){
        Payment payment = new Payment(id,null);
        return new CommonResult<>(400,"block handler: "+e.getMessage(),payment);
    }

    @Resource
    PaymentService paymentService;


    @GetMapping(value = "/consumer/paymentSQL/{id}")
    public CommonResult<Payment> paymentSQL(@PathVariable("id") Long id){
        return paymentService.paymentSQL(id);
    }
}
