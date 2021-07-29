package com.cnion.mybatisdemo.mpc.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.UUID;

/**
 * @description: TODO
 * @author: zero
 * @date: 2021/7/20 17:08
 * @version: 1.0
 */
public class GenernateUUID {

    /**
     * 生成UUID工具类
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }



    public static void main(String[] args){
        System.out.println("test==========="+getUUID());
        System.out.println("test==========="+getUUID());
        System.out.println("test==========="+getUUID());
    }
}
