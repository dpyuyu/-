package com.websocker.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


@Data
@Builder
public class OrderDTO extends WsBaseDTO implements Serializable {
    private static final long serialVersionUID = -39605303957850L;
    //登录
    public static final Integer LOGIN = 5;
    //登录冲突
    public static final Integer LOGIN_CONFLICT = 6;

    public OrderDTO() {
    }

    public OrderDTO(String msg, Integer code, String msgJson, String topic) {
        super(msg, code, msgJson, topic);
    }
}
