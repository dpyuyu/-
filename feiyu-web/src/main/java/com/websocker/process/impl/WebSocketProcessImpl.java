package com.websocker.process.impl;

import com.alibaba.fastjson.JSON;
import com.common.WebSocketException;
import com.websocker.dto.*;
import com.websocker.process.IWebSocketProcess;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Log4j2
public class WebSocketProcessImpl implements IWebSocketProcess {

    @Autowired
    private StringRedisTemplate redisTemplate;

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
            case WsDTO.USER_CHAT_LOGIN:
                this.chat(ctx, dto);
                break;
            default:
        }

    }


    private void chat(ChannelHandlerContext ctx, WsBaseDTO dto) throws WebSocketException {
        ChatDTO chatDTO = JSON.parseObject(dto.getMsgJson(), ChatDTO.class);
        if (chatDTO == null) {
            log.info("error chatDTO  chatDTO{}", chatDTO);
            throw new WebSocketException("聊天参数异常");
        }
        LoginConflict(ctx,chatDTO.getUserId(),chatDTO.getUserClientId());


        WsDTO.clientMap.put(chatDTO.getUserClientId(), ctx);
        ConcurrentHashMap<Long, ChannelHandlerContext> map = new ConcurrentHashMap<>();
        map.put(chatDTO.getUserClientId(), ctx);
        WsDTO.userMap.put(chatDTO.getUserId(), map);
        WsDTO.remoteAddress.put(chatDTO.getUserClientId(),ctx.channel().remoteAddress());
        ctx.write(new TextWebSocketFrame(JSON.toJSONString(ChatDTO.builder().typeCode(OrderDTO.LOGIN).build())));
    }


    /**
     * 登录冲突T人 只是t相同客户端  不同客户端不t
     *
     * @param redisKey
     * @param userClientId
     */
    private void LoginConflict(ChannelHandlerContext ctxL,Long redisKey, Long userClientId) {
        ConcurrentHashMap<Long, ChannelHandlerContext> map = WsDTO.userMap.get(redisKey);
        if (map!=null) {
            ConcurrentHashMap.KeySetView<Long, ChannelHandlerContext> longs = map.keySet();

            //判断是否有 相同的客户端登录并且 地址不同
            if (longs != null && longs.contains(userClientId)&&!WsDTO.remoteAddress.get(userClientId).equals(ctxL.channel().remoteAddress())) {
                log.info("LOGIN_CONFLICT userClientId{}", userClientId);
                ChannelHandlerContext ctx = WsDTO.clientMap.get(userClientId);
                ctx.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(ChatDTO.builder().typeCode(OrderDTO.LOGIN_CONFLICT).build())));
                //ctx.close();
            }
        }
        return;
    }


}
