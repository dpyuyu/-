package com.websocker.process.impl;

import com.alibaba.fastjson.JSON;
import com.chat.process.ChatProcess;
import com.common.WebSocketException;
import com.feiyu.netty.dto.UserMessage;
import com.feiyu.netty.dto.WsDTO;
import com.user.service.ITUserFriendRelationService;
import com.websocker.process.IWebSocketProcess;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class WebSocketProcessImpl implements IWebSocketProcess {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ITUserFriendRelationService friendRelationService;


    public void searchOrder(ChannelHandlerContext ctx, WebSocketFrame frame) throws WebSocketException {

        String request = ((TextWebSocketFrame) frame).text();
        UserMessage dto = JSON.parseObject(request, UserMessage.class);

        switch (dto.getTopic()) {
            case WsDTO.USER_MSG:
                ChatProcess.chat(ctx, dto);
                break;
            default:
        }

    }
}



