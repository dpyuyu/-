package com.feiyu.netty.dto;


import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class BaseVO implements Serializable {
    private static final long serialVersionUID = -3946734305303957850L;
    @Builder.Default
    private String msg = "操作成功";
    @Builder.Default
    private String code = "1";

    private String msgJson;

    private String topic;

    private String serverName;

    private String serverIp;

    private String clientId;

    private Long  userId;


}
