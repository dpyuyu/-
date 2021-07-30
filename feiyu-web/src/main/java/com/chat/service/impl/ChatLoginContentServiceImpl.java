package com.chat.service.impl;

import com.alibaba.fastjson.JSON;
import com.chat.ChatType;
import com.chat.service.IChatService;
import com.common.ChatAnnotation;
import com.common.ICommonSockerProcess;
import com.common.WebSocketException;
import com.fasterxml.jackson.core.io.CharTypes;
import com.websocker.dto.ChatDTO;
import com.websocker.dto.WsDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.swagger.annotations.Tag;
import javafx.scene.chart.Chart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@ChatAnnotation(value ={ChatType.LOGIN})
@Slf4j
public class ChatLoginContentServiceImpl extends ICommonSockerProcess implements IChatService {

    @Override
    public void issue(ChannelHandlerContext ctx, ChatDTO chatDTO) throws WebSocketException {

        switch (chatDTO.getType()){
            case ChatType.LINK:
                link(ctx,chatDTO);
                break;
            case ChatType.USER_CONTENT:
                content(ctx,chatDTO);
                break;
        }




    }

    private void content(ChannelHandlerContext ctx, ChatDTO chat) throws WebSocketException {
        if (chat == null) {
            log.info("error chatDTO  chatDTO{}", chat);
            throw new WebSocketException("聊天参数异常");
        }
        sendContentUserToId(chat.getUserId(),chat.getUserToId(),chat.getContent());
    }

    private void link(ChannelHandlerContext ctx, ChatDTO chatDTO) {
        LoginConflict(ctx, chatDTO.getUserId(), chatDTO.getUserClientId());
        //存储一些信息
        WsDTO.clientMap.put(chatDTO.getUserClientId(), ctx);  //存clientId 的地址
        ConcurrentHashMap<String, ChannelHandlerContext> map = new ConcurrentHashMap<>();
        map.put(chatDTO.getUserClientId(), ctx);
        WsDTO.userMap.put(chatDTO.getUserId(), map);  //按照userid 存多个客户端地址
        WsDTO.remoteAddress.put(chatDTO.getUserClientId(), ctx.channel().remoteAddress()); //按照clientId 存远程地址
        ctx.write(new TextWebSocketFrame(JSON.toJSONString(ChatDTO.builder().content("登录成功").typeCode(ChatType.LINK).build())));
    }


}
