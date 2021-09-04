package com.user.dto;

import com.user.entity.TUserInfo;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class UserBaseDTO implements Serializable {
    private static final long serialVersionUID = -39467343957850L;
    @Builder.Default
    private String msg = "操作成功";
    @Builder.Default
    private Integer code = 1;


    private TUserInfo userInfo;
    private List<TUserInfo> userInfos;
}
