package com.websocker.dto;

import io.netty.channel.ChannelHandlerContext;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class WsDTO extends WsBaseDTO {

  public static  ConcurrentHashMap<Long,ChannelHandlerContext> clientMap =  new ConcurrentHashMap<Long,ChannelHandlerContext>();
  public static  ConcurrentHashMap<Long, SocketAddress> remoteAddress =  new ConcurrentHashMap<Long,SocketAddress>();
  public static  ConcurrentHashMap<Long,ConcurrentHashMap<Long,ChannelHandlerContext>> userMap =  new ConcurrentHashMap<Long,ConcurrentHashMap<Long,ChannelHandlerContext>>();

  /**
   * 用户聊天登录
   */
  public static final String USER_CHAT_LOGIN = "chat_login";

}
