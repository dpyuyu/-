package com.fieyu.netty.process;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public interface IWebSocketProcess  {
    void searchOrder(ChannelHandlerContext ctx, WebSocketFrame frame);
}
