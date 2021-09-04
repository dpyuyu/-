package com.chat.service.impl;

import com.alibaba.fastjson.JSON;
import com.chat.ChatType;
import com.chat.service.IChatService;
import com.common.ChatAnnotation;
import com.common.ICommonSockerProcess;
import com.common.WebSocketException;
import com.websocker.dto.UserDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@ChatAnnotation(value ={ChatType.LINK})
@Slf4j
public class ChatLoginContentServiceImpl extends ICommonSockerProcess implements IChatService {

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void issue(ChannelHandlerContext ctx, UserDTO chat) throws WebSocketException {

        switch (chat.getType()){
            case ChatType.LINK:
                link(ctx,chat);
                break;
            case ChatType.USER_CONTENT:
                content(ctx,chat);
                break;
            case ChatType.LINK_OUT:
                linkOut(ctx,chat);
                break;
        }
    }

    private void linkOut(ChannelHandlerContext ctx, UserDTO chat) {
        //发送断线信息
        sendContentUserToId(chat.getUserId(),chat.getUserId(),chat.getContent(),chat.getType());
        //清除在线状态
        saveLoginOut(chat.getUserId());
        //关闭链接
        ctx.close();
    }


    private void content(ChannelHandlerContext ctx, UserDTO chat) throws WebSocketException {
        if (chat == null) {
            log.info("error chatDTO  chatDTO{}", chat);
            throw new WebSocketException("聊天参数异常");
        }
        sendContentUserToId(chat.getUserId(),chat.getUserToId(),chat.getContent(),chat.getType());

      //  redisTemplate.opsForValue().setIfPresent("key","value",200, TimeUnit.HOURS); // 设置这个key 过期时间为 x
    }


    private void link(ChannelHandlerContext ctx, UserDTO userDTO) {
        //保存用户在线信息
        this.saveLogin(userDTO.getUserId());
        log.info("user link success userDTO{}",userDTO);
    }




}
