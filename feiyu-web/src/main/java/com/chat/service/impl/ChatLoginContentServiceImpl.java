package com.chat.service.impl;


import com.chat.ChatType;
import com.chat.service.IChatService;
import com.common.ChatAnnotation;
import com.common.ICommonSockerProcess;
import com.common.WebSocketException;
import com.feiyu.netty.dto.UserMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@ChatAnnotation(value ={ChatType.LINK})
@Slf4j
public class ChatLoginContentServiceImpl extends ICommonSockerProcess implements IChatService {

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void issue(ChannelHandlerContext ctx, UserMessage chat) throws WebSocketException {

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

    private void linkOut(ChannelHandlerContext ctx, UserMessage chat) {
        //发送断线信息
        sendContentUserToId(chat.getUserId(),chat.getUserId(),chat.getContent(),chat.getType());
        //清除在线状态
        saveLoginOut(chat.getUserId());
        //关闭链接
        ctx.close();
    }


    private void content(ChannelHandlerContext ctx, UserMessage chat) throws WebSocketException {
        if (chat == null) {
            log.info("error chatDTO  chatDTO{}", chat);
            throw new WebSocketException("聊天参数异常");
        }
        sendContentUserToId(chat.getUserId(),chat.getUserToId(),chat.getContent(),chat.getType());

      //  redisTemplate.opsForValue().setIfPresent("key","value",200, TimeUnit.HOURS); // 设置这个key 过期时间为 x
    }


    private void link(ChannelHandlerContext ctx, UserMessage userMessage) {
        //保存用户在线信息
        this.saveLogin(userMessage.getUserId());
        log.info("user link success userDTO{}", userMessage);
    }




}
