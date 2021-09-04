package com.fieyu.service.impl;

import com.alibaba.fastjson.JSON;
import com.fieyu.netty.dto.BaseVO;
import com.fieyu.netty.dto.UserDTO;
import com.fieyu.netty.dto.ConsumerType;
import com.fieyu.netty.dto.WsDTO;
import com.fieyu.service.IConsumer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ConsumerImpl implements IConsumer {

    @Override
    public void consumerMsg(ChannelHandlerContext ctx, BaseVO dto) {
        log.info("consumerMsg{}",dto);
        UserDTO user = this.getUserDTO(dto);
        //todo 如果chat等于null 直接通过用户ctx返回操作失败 code0 不返回具体参数
        switch (user.getType()) {
            case ConsumerType.LINK:
                //登录消息
                this.link(ctx, user);
                break;
            case ConsumerType.LINK_OUT:
                //登出消息
                this.linkOut(ctx, user);
                break;
            default:
                //默认消息
                this.serverSideProcessing(user);
                break;
        }
    }

    @Override
    public void serverSideProcessing(UserDTO chat) {
        //一个简单的负载均衡

        int size = WsDTO.serverCtx.size();
        if (size == 0) {
            log.info("server link success number is null");
            return;
        }
        if ((WsDTO.LOAD_BALANCING + 1) == size) {
            WsDTO.LOAD_BALANCING = 0;
        }

        Channel channel = WsDTO.serverCtx.get(WsDTO.LOAD_BALANCING);

        log.info("serverSideProcessing public{}", chat);
        channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(chat)));
    }

    private void linkOut(ChannelHandlerContext ctx, UserDTO chat) {
        log.info("user linkOut chat{}", chat.getUserClientId());
        //清除 服务处理程序
        WsDTO.clientMap.remove(chat.getUserClientId());
        WsDTO.clientRemoteAddress.remove(chat.getUserClientId(), ctx.channel().remoteAddress());
        WsDTO.userMap.remove(chat.getUserId());
        //发送信息给服务处理程序
        this.serverSideProcessing(chat);
        log.info("user linkOut success chat{}", chat.getUserClientId());
    }


    private void link(ChannelHandlerContext ctx, UserDTO chat) {
        log.info("user link chat{}", chat.getUserClientId());
        //登录冲突 清除服务端用户缓存
        this.LoginConflict(ctx, chat.getUserId(), chat.getUserClientId());
        //存储客户端通道信息
        WsDTO.clientMap.put(chat.getUserClientId(), ctx);
        WsDTO.clientRemoteAddress.put(chat.getUserClientId(), ctx.channel().remoteAddress());
        ConcurrentHashMap<String, ChannelHandlerContext> map = new ConcurrentHashMap<>();
        map.put(chat.getUserClientId(), ctx);
        WsDTO.userMap.put(chat.getUserId(), map);
        //发送信息给服务处理程序
        this.serverSideProcessing(chat);
        //通知服务处理Web端处理程序
        log.info("user success chat{}", chat.getUserClientId());
    }

    private UserDTO getUserDTO(BaseVO dto) {
        return JSON.parseObject(dto.getMsgJson(), UserDTO.class);
    }

    /**
     * 登录冲突T人
     *
     * @param userId
     * @param userClientId
     */
    protected Boolean LoginConflict(ChannelHandlerContext ctxL, Long userId, String userClientId) {
        ConcurrentHashMap<String, ChannelHandlerContext> map = WsDTO.userMap.get(userId);
        if (map != null) {
            ConcurrentHashMap.KeySetView<String, ChannelHandlerContext> longs = map.keySet();
            //判断是否有 相同的客户端登录并且 地址不同
            if (longs != null && longs.contains(userClientId) && !WsDTO.clientRemoteAddress.get(userClientId).equals(ctxL.channel().remoteAddress())) {
                log.info("LOGIN_CONFLICT userId{},userClientId{}", userId, userClientId);
                ChannelHandlerContext ctx = WsDTO.clientMap.get(userClientId);
                ctx.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(UserDTO.builder()
                        .content("登录冲突")
                        .typeCode(ConsumerType.LOGIN_CONFLICT)
                        .build())));
                return true;
            }
        }
        return false;
    }

}

