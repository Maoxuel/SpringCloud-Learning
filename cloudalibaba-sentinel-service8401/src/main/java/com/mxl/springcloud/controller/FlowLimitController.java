package com.mxl.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class FlowLimitController {

    @GetMapping("/testA")
    public String testA() {
        return "test A";
    }

    @GetMapping("/testB")
    public String testB() {
        return "test B";
    }

    //RT测试
    @GetMapping("/testC")
    public String testC() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("****test C");
        return "test C";
    }

    //异常比例、异常数测试
    @GetMapping("/testD")
    public String testD(){
        log.info("test D");
        int i = 10/0;
        return "test D";
    }

    //测试热点key
    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey",blockHandler = "deal_hotKey")
    public String testHotKey(@RequestParam(value = "p1",required = false) String p1,
                             @RequestParam(value = "p2",required = false) String p2){
        return "test HOtKey";
    }

    public String deal_hotKey(String p1, String p2, BlockException ex){
        return "deal hotKey"; //替换sentinel默认提示
    }
}
