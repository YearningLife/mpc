package com.cnion.mybatisdemo.mpc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description: TODO
 * @author: zero
 * @date: 2021/7/20 15:05
 * @version: 1.0
 */
@SpringBootApplication
@MapperScan("com.cnion.mybatisdemo.mpc")
public class MpcApplication {
    public static void main(String[] args) {
        SpringApplication.run(MpcApplication.class,args);
    }
}
