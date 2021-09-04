package com.fieyu.netty.dto;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

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

    @ApiModelProperty("目标用户id")
    private String userName;

    @ApiModelProperty("类型编号")
    private String typeCode;

    @ApiModelProperty("聊天内容")
    private String content;

    @ApiModelProperty("状态")
    private String type;

    @ApiModelProperty("聊天内容")
    private String topic;

    @ApiModelProperty("需要发送的所有用户")
    private List<String> userClientIds;

    public static void main(String[] args) {
        UserDTO dto = UserDTO.builder()
                .userClientId("web1")
                .userId(1l)
                .type(ConsumerType.LINK)
              //  .userToClientId("2")
               // .userToId(2l)
                .userName("丽敏")
            //    .content("请输入你的聊天内容")
                .topic("chat")
                .build();

        BaseVO baseDTO = new BaseVO();
        baseDTO.setTopic(WsDTO.USER_MSG);
        baseDTO.setMsgJson(JSON.toJSONString(dto));
        baseDTO.setServerName("chat_server");

        System.out.println(JSON.toJSONString(baseDTO));
        System.out.println(JSON.toJSONString(dto));
    }
}
