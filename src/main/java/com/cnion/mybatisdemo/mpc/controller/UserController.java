package com.cnion.mybatisdemo.mpc.controller;


import com.cnion.mybatisdemo.mpc.entity.User;
import com.cnion.mybatisdemo.mpc.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author qpf
 * @since 2021-07-20
 */
@RestController
@RequestMapping("/mpc/user")
public class UserController {

    @Autowired
    private IUserService userServiceImpl;

    @GetMapping("/saveUser")
    public Map<String,Object> saveUserInfo(){
        HashMap<String, Object> map = new HashMap<>();
        User user = new User();
        boolean save = userServiceImpl.save(user);
        if (!save) {
            map.put("E100","添加失败");
        }
        return map;
    }
}
