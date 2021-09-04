package com.websocker.dto;

import com.alibaba.fastjson.JSON;
import com.chat.ChatType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Data
@ToString
@Builder
public class UserDTO implements Serializable {
    private static final long serialVersionUID = -394605303957850L;



    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户id")
    private String userClientId;

    @ApiModelProperty("目标用户id")
    private String userToClientId;

    @ApiModelProperty("目标用户id")
    private Long userToId;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("类型编号")
    private String typeCode;

    @ApiModelProperty("聊天内容")
    private String content;

    @ApiModelProperty("聊天内容")
    private String type;

    @ApiModelProperty("聊天内容")
    private String topic;

    public static void main(String[] args) {
        UserDTO dto = UserDTO.builder()
                .userClientId("web1")
                .userId(1l)
                .type(ChatType.LINK_OUT)
                .userToClientId("2")
                .userToId(2l)
                .userName("你好")
                .content("请输入你的聊天内容")
                .build();

        WsBaseDTO baseDTO = new WsBaseDTO();
        baseDTO.setTopic(WsDTO.CHAT);
        baseDTO.setMsgJson(JSON.toJSONString(dto));

        System.out.println(JSON.toJSONString(baseDTO));
        System.out.println(JSON.toJSONString(dto));
    }
}
