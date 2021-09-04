package com.user.dto;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRelationDTO implements Serializable {

    private static final long serialVersionUID = 123123123L;


    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiParam(hidden = true)
    private Long userIdFriend;

    @ApiModelProperty(value = "用户id")
    private String userPhone;


    @ApiModelProperty(value = "好友")
    private String userPhoneFriend;

    @ApiModelProperty(value = "是否拉取到黑名单")
    private String isBlacklist;

    @ApiModelProperty(value = "好友状态")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime modifyTime;

    @ApiModelProperty(value = "是否在线")
    private String isOnline;

}
