package com.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author jiabao
 * @since 2021-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_user_friend_relation")
@ApiModel(value = "TUserFriendRelation对象", description = "")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TUserFriendRelation implements Serializable {

    private static final long serialVersionUID = 123123123L;


    @ApiModelProperty(value = "用户id")
    @ApiParam(hidden = true)
    @JsonIgnore
    private Long userId;

    @ApiModelProperty(value = "好友")
    @ApiParam(hidden = true)
    @JsonIgnore
    private Long userIdFriend;

    @ApiModelProperty(value = "用户id")
    private String userPhone;


    @ApiModelProperty(value = "好友")
    private String userPhoneFriend;

    @ApiModelProperty(value = "是否拉取到黑名单")
    @ApiParam(hidden = true)
    @JsonIgnore
    private String isBlacklist;

    @ApiModelProperty(value = "好友状态")
    @ApiParam(hidden = true)
    @JsonIgnore
    private String status;

    @ApiModelProperty(value = "创建时间")
    @ApiParam(hidden = true)
    @JsonIgnore
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    @ApiParam(hidden = true)
    @JsonIgnore
    private LocalDateTime modifyTime;

}
