package com.chat.service;

import com.common.WebSocketException;
import com.websocker.dto.ChatDTO;
import io.netty.channel.ChannelHandlerContext;

public interface IChatService {

    void issue(ChannelHandlerContext ctx, ChatDTO chatDTO) throws WebSocketException;
}
