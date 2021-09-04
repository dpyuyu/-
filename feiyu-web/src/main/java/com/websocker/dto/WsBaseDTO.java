package com.websocker.dto;


import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class WsBaseDTO implements Serializable {
    private static final long serialVersionUID = -3946734305303957850L;
    private String msg= "操作成功";

    private Integer code= 1;

    private String msgJson;

    private String topic;

    private String serverName;
}
