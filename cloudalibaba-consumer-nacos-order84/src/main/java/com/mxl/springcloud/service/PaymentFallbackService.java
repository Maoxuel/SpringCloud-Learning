package com.mxl.springcloud.service;

import com.mxl.springcloud.entities.CommonResult;
import com.mxl.springcloud.entities.Payment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Component
public class PaymentFallbackService implements PaymentService {
    @Override
    public CommonResult<Payment> paymentSQL(@PathVariable("id") Long id) {
        return new CommonResult<>(400,"paymentFallbackService",new Payment(id,"error001"));
    }
}
