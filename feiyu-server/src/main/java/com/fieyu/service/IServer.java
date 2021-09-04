package com.fieyu.service;

import com.fieyu.netty.dto.BaseVO;
import io.netty.channel.ChannelHandlerContext;

public interface IServer {
    void serverLogin(ChannelHandlerContext ctx, BaseVO dto);


    /**
     * 处理服务端向客户端发送的信息
     * @param dto
     */
    void serverMsg(BaseVO dto);

}
