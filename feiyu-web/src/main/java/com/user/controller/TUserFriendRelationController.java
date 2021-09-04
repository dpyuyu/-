package com.user.controller;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.user.dto.UserBaseDTO;
import com.user.dto.UserRelationDTO;
import com.user.entity.TUserFriendRelation;
import com.user.entity.TUserInfo;
import com.user.service.ITUserFriendRelationService;
import com.user.service.ITUserInfoService;
import com.user.userdto.UserDTO;
import com.user.userdto.UserStatusDTO;
import com.websocker.dto.RedisKeys;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jiabao
 * @since 2021-07-27
 */
@RestController
@RequestMapping("/web/user/t-user-friend-relation")
@Api(tags = "用户好友操纵")
public class TUserFriendRelationController {

    @Autowired
    private ITUserFriendRelationService friendRelationService;

    @Autowired
    private ITUserInfoService userInfoService;


    @Autowired
    private RedisTemplate redisTemplate;



    @GetMapping("/get_user_friends")
    @ApiOperation(value = "拉取用户好友列表")
    public UserDTO getFreiends(Long userId){
        List<TUserFriendRelation> relations = friendRelationService.getBaseMapper().selectList(Wrappers.<TUserFriendRelation>lambdaQuery()
                .eq(TUserFriendRelation::getUserId, userId)
                .eq(TUserFriendRelation::getStatus, UserStatusDTO.NORMAL)
                .ne(TUserFriendRelation::getIsBlacklist, UserStatusDTO.DELETE)
        );


        List<String> userString = redisTemplate.opsForHash().values(RedisKeys.USER_CHAT_LOGIN);
        if (CollectionUtils.isEmpty(userString)){
            userString = new ArrayList<String>();
        }
        List<Long> users = userString.stream().map(x -> Long.valueOf(x)).collect(Collectors.toList());

        List<UserRelationDTO> userRelationDTOS = relations.stream().map(x -> UserRelationDTO.builder()
                .userId(x.getUserId())
                .isOnline(users.contains(x.getUserIdFriend()) ? "1" : "0")
                .userPhone(x.getUserPhone())
                .userIdFriend(x.getUserIdFriend())
                .userPhoneFriend(x.getUserPhone())
                .build()).collect(Collectors.toList());

        return UserDTO.builder()
                .relationsDTO(userRelationDTOS)
                .build();
    }



    @PostMapping("/add")
    @ResponseBody
    @ApiOperation(value = "添加好友")

    public UserBaseDTO add(TUserFriendRelation relation) {

        TUserInfo tUserInfo = userInfoService.selectByPhone(relation.getUserPhoneFriend());
        //判断是否存在好友关系
        if (tUserInfo!=null){
            relation.setModifyTime(LocalDateTime.now());
            relation.setCreateTime(LocalDateTime.now());
            relation.setStatus("0");
            relation.setIsBlacklist("0");
            friendRelationService.saveRelation(relation);
            return UserBaseDTO.builder().build();
        }
        return UserBaseDTO.builder().code(0).msg("操作失败").build();
    }





}
