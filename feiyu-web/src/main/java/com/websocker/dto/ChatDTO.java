package com.websocker.dto;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Data
@ToString
@Builder
public class ChatDTO extends WsBaseDTO implements Serializable {
    private static final long serialVersionUID = -394605303957850L;
    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户id")
    private Long userClientId;

    @ApiModelProperty("目标用户id")
    private Long userToClientId;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("类型编号")
    private Integer typeCode;

    public static void main(String[] args) {
        ChatDTO dto = ChatDTO.builder()
                .userClientId(1l)
                .userId(1l)
                .userToClientId(1l)
                .userName("你好")
                .build();

        WsBaseDTO baseDTO = new WsBaseDTO();
        baseDTO.setTopic(WsDTO.USER_CHAT_LOGIN);
        baseDTO.setMsgJson(JSON.toJSONString(dto));



        System.out.println(JSON.toJSONString(baseDTO));
    }
}
