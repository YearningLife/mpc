package com.cnion.mybatisdemo.mpc.service;

import com.cnion.mybatisdemo.mpc.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qpf
 * @since 2021-07-20
 */
public interface IUserService extends IService<User> {

    boolean sava(User user);

}
