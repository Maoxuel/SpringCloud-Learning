package com.mxl.springcloud.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.mxl.springcloud.dao")
public class MyBatisConfig {
}
