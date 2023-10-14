package com.shill.gran.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author zhongzb
 * @date 2021/05/27
 */
@SpringBootApplication(scanBasePackages = {"com.shill.gran.common"})
@MapperScan({"com.shill.gran.common.**.mapper"})
@ServletComponentScan
public class GranCustomApplication {

    public static void main(String[] args) {
        SpringApplication.run(GranCustomApplication.class,args);
    }

}