package com.user.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.user.dto.UserBaseDTO;
import com.user.entity.TUserFriendRelation;
import com.user.entity.TUserInfo;
import com.user.service.ITUserFriendRelationService;
import com.user.service.ITUserInfoService;
import com.websocker.dto.WsBaseDTO;
import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jiabao
 * @since 2021-07-27
 */
@RestController
@RequestMapping("/web/user/t-user-info")
@Api(tags = "用户信息")
@AllArgsConstructor
@NoArgsConstructor
public class TUserInfoController {

    @Autowired
    private ITUserInfoService userInfoService;


    @GetMapping("/get_user_info")
    @ApiOperation(value = "拉取个人信息")
    public UserBaseDTO getUserInfo(String phone) {
        TUserInfo tUserInfo =  userInfoService.selectByPhone(phone);


        return UserBaseDTO.builder().userInfo(tUserInfo).build();
    }

    @GetMapping("/get_user_infos")
    @ApiOperation(value = "拉取多个人信息")
    public UserBaseDTO getUserInfoByPhone(@Param("phones") Long[] phones) {
        List<TUserInfo> tUserInfos = userInfoService.getBaseMapper().selectList(Wrappers.<TUserInfo>lambdaQuery().in(TUserInfo::getPhone, phones));
        return UserBaseDTO.builder().userInfos(tUserInfos).build();
    }

    @PostMapping("/login")
    public UserBaseDTO login(String phone, String password){
        Boolean login = userInfoService.selectUser(phone,password);
        if (login){
            return UserBaseDTO.builder().build();
        }
        return UserBaseDTO.builder().code(1).msg("用户名或密码不正确").build();
    }


    @PostMapping("/registered")
    public UserBaseDTO registered(String phone, String password){
        //判断该用户手机号是否存在
        TUserInfo tUserInfo = userInfoService.selectByPhone(phone);
        if (tUserInfo!=null){
            return UserBaseDTO.builder().code(0).msg("该用户信息已存在").build();
        }

        TUserInfo registered = userInfoService.registered(phone, password);
        return UserBaseDTO.builder().code(0).userInfo(registered).msg("用户名或密码不正确").build();
    }
}
