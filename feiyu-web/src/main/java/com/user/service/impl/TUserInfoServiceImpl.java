package com.user.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.el.stream.Optional;
import com.user.dto.UserDTO;
import com.user.entity.TUserInfo;
import com.user.mapper.TUserInfoMapper;
import com.user.service.ITUserInfoService;
import com.user.userdto.UserStatusDTO;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiabao
 * @since 2021-07-27
 */
@Service
public class TUserInfoServiceImpl extends ServiceImpl<TUserInfoMapper, TUserInfo> implements ITUserInfoService {

    @Override
    public TUserInfo selectByPhone(String phone) {
       return  this.getBaseMapper().selectOne(Wrappers.<TUserInfo>lambdaQuery()
                .eq(TUserInfo::getPhone,phone)
               .ne(TUserInfo::getStatus, UserStatusDTO.DELETE)
        );

    }

    @Override
    public Boolean selectUser(String phone, String password) {
        TUserInfo tUserInfo = this.getBaseMapper().selectOne(Wrappers.<TUserInfo>lambdaQuery()
                .eq(TUserInfo::getPhone, phone)
                .eq(TUserInfo::getPassword, password)
                .ne(TUserInfo::getStatus, UserDTO.STATUS_D)
        );
        return  tUserInfo!=null?true:false;
    }

    @Override
    public TUserInfo registered(String phone, String password) {
        TUserInfo build = TUserInfo.builder()
                .phone(phone)
                .password(password)
                .createTime(LocalDateTime.now())
                .modifyTime(LocalDateTime.now())
                .build();
       this.save(build);

        return build;
    }
}
