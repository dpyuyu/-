package com.common;

import com.alibaba.fastjson.JSON;
import com.feiyu.netty.dto.UserMessage;
import com.feiyu.netty.dto.WsDTO;
import com.websocker.dto.RedisKeys;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Log4j2
public  abstract class ICommonSockerProcess {

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 发送给指定的人 指定信息
     * @param userId
     * @param userToId
     * @param content
     * @param type
     */
    protected static void sendContentUserToId(Long userId, Long userToId, String content, String type) {
        ConcurrentHashMap<String, ChannelHandlerContext> map = WsDTO.userMap.get(userToId);
        Collection<ChannelHandlerContext> values = map.values();

        for (ChannelHandlerContext value : values) {
            value.writeAndFlush(new TextWebSocketFrame(
                    JSON.toJSONString(UserMessage.builder()
                            .userId(userId)
                            .userToId(userToId)
                            .content(content)
                            .typeCode(type).build()
                    )));
        }
    }


    protected  void saveLogin(Long userId){
            String redisKeys = String.format(RedisKeys.USER_IS_ONLINE,userId);
            redisTemplate.opsForHash().put(RedisKeys.USER_CHAT_LOGIN,redisKeys, com.user.dto.UserDTO.IS_ONLINE);
            redisTemplate.expire(redisKeys,600, TimeUnit.SECONDS);
    };

    protected  void saveLoginOut(Long userId){
        String redisKeys = String.format(RedisKeys.USER_IS_ONLINE,userId);
        redisTemplate.opsForHash().delete(RedisKeys.USER_CHAT_LOGIN,redisKeys);
    }





}
