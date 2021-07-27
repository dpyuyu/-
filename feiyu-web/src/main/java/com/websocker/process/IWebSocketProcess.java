package com.websocker.process;

import com.common.WebSocketException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public interface IWebSocketProcess {
    public void searchOrder(ChannelHandlerContext ctx, WebSocketFrame frame)throws WebSocketException;
}
