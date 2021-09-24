package com.chat.service;

import com.common.WebSocketException;

import com.feiyu.netty.dto.UserMessage;
import io.netty.channel.ChannelHandlerContext;

public interface IChatService {

    void issue(ChannelHandlerContext ctx, UserMessage userDTO) throws WebSocketException;
}
