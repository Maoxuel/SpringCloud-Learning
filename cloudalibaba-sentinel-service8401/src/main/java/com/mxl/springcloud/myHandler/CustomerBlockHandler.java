package com.mxl.springcloud.myHandler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.mxl.springcloud.entities.CommonResult;

public class CustomerBlockHandler {

    public static CommonResult handleException1(BlockException exception){
        return new CommonResult(400,"用户自定义全局blockHandler------1");
    }
    public static CommonResult handleException2(BlockException exception){
        return new CommonResult(400,"用户自定义全局blockHandler------2");
    }
}
