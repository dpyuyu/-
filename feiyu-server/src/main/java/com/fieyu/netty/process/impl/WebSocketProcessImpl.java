package com.fieyu.netty.process.impl;

import com.alibaba.fastjson.JSON;
import com.fieyu.netty.dto.BaseVO;
import com.fieyu.netty.dto.WsDTO;
import com.fieyu.netty.process.IWebSocketProcess;
import com.fieyu.service.IConsumer;
import com.fieyu.service.IServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSocketProcessImpl implements IWebSocketProcess {

    @Autowired
    private IServer server;
    @Autowired
    private IConsumer consumer;


    @Override
    public void searchOrder(ChannelHandlerContext ctx, WebSocketFrame frame) {

        BaseVO dto = this.getBaseDTO(frame);

        switch (dto.getTopic()) {
            case WsDTO.USER_MSG:
                //用户链接 --处理用户-server-web端处理程序
                consumer.consumerMsg(ctx,dto);break;
            case WsDTO.SEVER_LOGIN:
                //服务器链接-login--处理服务器端链接的服务
                server.serverLogin(ctx, dto);break;
            case WsDTO.SEVER_MSG:
                //处理服务器信息聊天-web-server-用户端程序
                server.serverMsg(dto);break;
        }
    }






    //判断请求是是客户端链接还是服务端链接
    private BaseVO getBaseDTO(WebSocketFrame frame) {
        String request = ((TextWebSocketFrame) frame).text();
        return JSON.parseObject(request, BaseVO.class);
    }

}
