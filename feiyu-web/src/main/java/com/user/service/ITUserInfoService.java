package com.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.user.entity.TUserInfo;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jiabao
 * @since 2021-07-27
 */
public interface ITUserInfoService extends IService<TUserInfo> {

    TUserInfo selectByPhone(String phone);

    /**
     * 判断用户是否有 信息
     * @param phone
     * @param password
     * @return
     */
    Boolean selectUser(String phone, String password);

    /**
     * 用户注册
     * @param phone
     * @param password
     */
    TUserInfo registered(String phone, String password);
}
