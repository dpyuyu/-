package com.common;

import com.alibaba.fastjson.JSON;
import com.chat.ChatType;
import com.websocker.dto.ChatDTO;
import com.websocker.dto.WsDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.log4j.Log4j2;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
public  abstract class ICommonSockerProcess {
    /**
     * 登录冲突T人
     *
     * @param userId
     * @param userClientId
     */
    protected static void LoginConflict(ChannelHandlerContext ctxL, Long userId, String userClientId) {
        ConcurrentHashMap<String, ChannelHandlerContext> map = WsDTO.userMap.get(userId);
        if (map!=null) {
            ConcurrentHashMap.KeySetView<String, ChannelHandlerContext> longs = map.keySet();
            //判断是否有 相同的客户端登录并且 地址不同
            if (longs != null && longs.contains(userClientId)&&!WsDTO.remoteAddress.get(userClientId).equals(ctxL.channel().remoteAddress())) {
                log.info("LOGIN_CONFLICT userClientId{}", userClientId);
                ChannelHandlerContext ctx = WsDTO.clientMap.get(userClientId);
                ctx.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(ChatDTO.builder()
                        .content("登录冲突")
                        .typeCode(ChatType.LOGIN_CONFLICT)
                        .build())));
                //ctx.close();
            }
        }
        return;
    }

    protected static void sendContentUserToId(Long userId, Long userToId, String content) {
        ConcurrentHashMap<String, ChannelHandlerContext> map = WsDTO.userMap.get(userToId);
        Collection<ChannelHandlerContext> values = map.values();

        for (ChannelHandlerContext value : values) {
            value.writeAndFlush(new TextWebSocketFrame(
                    JSON.toJSONString(ChatDTO.builder()
                            .userId(userId)
                            .userToId(userToId)
                            .content(content)
                            .typeCode(ChatType.USER_CONTENT).build()
                    )));
        }
    }
}
