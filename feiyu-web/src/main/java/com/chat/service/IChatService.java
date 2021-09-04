package com.chat.service;

import com.common.WebSocketException;
import com.websocker.dto.UserDTO;
import io.netty.channel.ChannelHandlerContext;

public interface IChatService {

    void issue(ChannelHandlerContext ctx, UserDTO userDTO) throws WebSocketException;
}
