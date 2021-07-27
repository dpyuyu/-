package com.user.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.user.entity.TUserFriendRelation;
import com.user.service.ITUserFriendRelationService;
import com.user.userdto.UserDTO;
import com.user.userdto.UserStatusDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jiabao
 * @since 2021-07-27
 */
@RestController
@RequestMapping("/user/t-user-friend-relation")
public class TUserFriendRelationController {

    @Autowired
    private ITUserFriendRelationService friendRelationService;


    @RequestMapping("/get_user_friends")
    @ApiOperation(value = "拉取用户好友列表",tags = "")
    public UserDTO getFreiends(Long userId){
        List<TUserFriendRelation> relations = friendRelationService.getBaseMapper().selectList(Wrappers.<TUserFriendRelation>lambdaQuery()
                .eq(TUserFriendRelation::getUserId, userId)
                .eq(TUserFriendRelation::getStatus, UserStatusDTO.NORMAL)
                .ne(TUserFriendRelation::getIsBlacklist, UserStatusDTO.DELETE)
        );

        return UserDTO.builder()
                .relations(relations)
                .build();
    }
}
