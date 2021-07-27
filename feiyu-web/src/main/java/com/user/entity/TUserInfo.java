package com.user.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@TableName("t_user_info")
@ApiModel(value = "TUserInfo对象", description = "")
public class TUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "头像")
    private String headImage;

    @ApiModelProperty(value = "电话号")
    private String phone;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime modifyTime;

    @ApiModelProperty(value = "账户余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "会员等级")
    private Integer member;

    @ApiModelProperty(value = "用户类型")
    private String type;

    @ApiModelProperty(value = "用户状态")
    private String status;

    @ApiModelProperty(value = "所在地")
    private String location;

    @ApiModelProperty(value = "签名")
    private String signature;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "密码")
    private String password;


}
