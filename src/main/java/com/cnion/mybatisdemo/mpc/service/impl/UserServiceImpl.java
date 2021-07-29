package com.cnion.mybatisdemo.mpc.service.impl;

import com.cnion.mybatisdemo.mpc.entity.User;
import com.cnion.mybatisdemo.mpc.mapper.UserMapper;
import com.cnion.mybatisdemo.mpc.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qpf
 * @since 2021-07-20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
