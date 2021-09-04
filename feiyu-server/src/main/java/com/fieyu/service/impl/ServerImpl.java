package com.fieyu.service.impl;

import com.alibaba.fastjson.JSON;
import com.fieyu.netty.dto.BaseVO;
import com.fieyu.netty.dto.UserDTO;
import com.fieyu.netty.dto.ConsumerType;
import com.fieyu.netty.dto.WsDTO;
import com.fieyu.service.IServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 负端ServerImpl 处理服务端向客户端发送消息的信息
 */
@Service
@Slf4j
public class ServerImpl implements IServer {



    @Override
    public void serverLogin(ChannelHandlerContext ctx, BaseVO dto) {
        log.info("server Login name{},remoteAddress{},localAddress{}",dto.getServerName(),ctx.channel().remoteAddress(),ctx.channel().localAddress());
        WsDTO.serverCtx.add(ctx.channel());
        log.info("server Login success ctxName:{}",dto.getServerName());
    }

    @Override
    public void serverMsg(BaseVO dto) {
        UserDTO userDTO = JSON.parseObject(dto.getMsgJson(), UserDTO.class);
        switch (userDTO.getType()){
            case ConsumerType.USER_ALL_ME_CONTENT:
                //发送聊天信息-sendAll--有指定用户信息
                this.sendAllMeMsg(userDTO);
            case ConsumerType.USER_ME_CONTENT:
                //将该条信息发送给自己
                 this.sendMeMsg(userDTO);
            case ConsumerType.USER_TO_CONTENT:
                //将该信息发送给指定用户
                this.sendToMsg(userDTO);
            case ConsumerType.USER_TO_All_CONTENT:
                //将信息发送至所有指定的用户
                this.sendToAllMsg(userDTO);
        }
    }

    private void sendToAllMsg(UserDTO userDTO) {
        List<String> userClientIds = userDTO.getUserClientIds();
        for (String userClientId : userClientIds) {
            log.info("sendToAllMsg{}", userDTO);
            ChannelHandlerContext context = WsDTO.clientMap.get(userClientId);
            if (context==null){
                log.info("sendToAllMsg ClinetId to channel is null{}", userDTO.getUserClientId());
            }
            context.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(userDTO)));
        }

    }

    private void sendToMsg(UserDTO userDTO) {
        log.info("sendToMsg{}", userDTO);
        ChannelHandlerContext context = WsDTO.clientMap.get(userDTO.getUserToClientId());
        if (context==null){
            log.info("sendToMsg ClinetId to channel is null{}", userDTO.getUserClientId());
        }
        context.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(userDTO)));
    }

    private void sendMeMsg(UserDTO userDTO) {
        log.info("sendMeMsg{}", userDTO);
        ChannelHandlerContext context = WsDTO.clientMap.get(userDTO.getUserClientId());
        if (context==null){
            log.info("sendMeMsg ClinetId to channel is null{}", userDTO.getUserClientId());
        }
        context.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(userDTO)));
    }

    private void sendAllMeMsg(UserDTO userDTO) {
        log.info("sendAllMsg{}" , userDTO);
        Collection<ConcurrentHashMap<String, ChannelHandlerContext>> values = WsDTO.userMap.values();
        for (ConcurrentHashMap<String, ChannelHandlerContext> value : values) {
            Collection<ChannelHandlerContext> contexts = value.values();
            for (ChannelHandlerContext context : contexts) {
                context.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(userDTO)));
            }
        }
    }

}

