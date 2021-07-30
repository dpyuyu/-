package com.websocker.process.impl;

import com.alibaba.fastjson.JSON;
import com.chat.ChatType;
import com.chat.process.ChatProcess;
import com.common.WebSocketException;
import com.user.entity.TUserFriendRelation;
import com.user.service.ITUserFriendRelationService;
import com.user.userdto.UserStatusDTO;
import com.websocker.dto.*;
import com.websocker.process.IWebSocketProcess;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Log4j2
public class WebSocketProcessImpl implements IWebSocketProcess {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ITUserFriendRelationService friendRelationService;


    public void searchOrder(ChannelHandlerContext ctx, WebSocketFrame frame) throws WebSocketException {

        String request = ((TextWebSocketFrame) frame).text();
        WsBaseDTO dto = null;
        if (request == null) {
            log.info("error request  request{}", request);
            throw new WebSocketException("参数为空");
        }
        dto = JSON.parseObject(request, WsBaseDTO.class);

        if (dto == null) {
            log.info("error BasDTO  BaseDTO{}", dto);
            throw new WebSocketException("参数异常");
        }

        switch (dto.getTopic()) {
            case WsDTO.CHAT:
                ChatProcess.chat(ctx, dto);
                break;
            default:
        }

    }
}



