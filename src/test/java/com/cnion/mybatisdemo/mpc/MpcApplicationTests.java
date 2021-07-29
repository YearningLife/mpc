package com.cnion.mybatisdemo.mpc;


import com.cnion.mybatisdemo.mpc.entity.User;
import com.cnion.mybatisdemo.mpc.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class MpcApplicationTests {

    @Autowired
    private UserMapper userMapper;
    //
    // @Resource
    // private MybatilsDataSourceEntity mybatilsDataSourceEntity;
    //
    @Test
    void contextLoads() {

    }
    // @Test
    // void testSelect(){
    //
    //     System.out.println("==============select all method data==============");
    //     List<User> userList = userMapper.selectList(null);
    //     for (User user : userList) {
    //         System.out.println(user.toString());
    //     }
    //
    // }

    // @Test
    // public void testPwd(){
    //     System.out.println(mybatilsDataSourceEntity.getPassword());
    // }

    @Test
    void testList(){
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

}
