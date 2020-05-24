package com.mxl.springcloud.controller;

import com.mxl.springcloud.entities.CommonResult;
import com.mxl.springcloud.entities.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    public static HashMap<Long, Payment> map = new HashMap<>();
    static {
        map.put(1L,new Payment(1L,"0000000000000001"));
        map.put(2L,new Payment(2L,"0000000000000002"));
        map.put(3L,new Payment(3L,"0000000000000003"));
    }

    @GetMapping(value = "/paymentSQL/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id){
        Payment payment = map.get(id);
        return new CommonResult<>(200,"from mysql,server port: "+serverPort,payment);
    }
}
