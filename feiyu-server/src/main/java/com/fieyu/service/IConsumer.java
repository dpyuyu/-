package com.fieyu.service;

import com.fieyu.netty.dto.BaseVO;
import com.fieyu.netty.dto.UserDTO;
import io.netty.channel.ChannelHandlerContext;

public interface IConsumer {
    /**
     * 用户处理程序
     * @param ctx
     * @param dto
     */
    void consumerMsg(ChannelHandlerContext ctx, BaseVO dto);

    /**
     * 服务端处理啊程序
     * @param chat
     */
    void serverSideProcessing( UserDTO chat);
}
