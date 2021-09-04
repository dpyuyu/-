package com.websocker.dto;

import io.netty.channel.ChannelHandlerContext;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class WsDTO extends WsBaseDTO {

  public static  ConcurrentHashMap<String ,ChannelHandlerContext> clientMap =  new ConcurrentHashMap<String, ChannelHandlerContext>();
  public static  ConcurrentHashMap<String, SocketAddress> remoteAddress =  new ConcurrentHashMap<String,SocketAddress>();
  public static  ConcurrentHashMap<Long,ConcurrentHashMap<String,ChannelHandlerContext>> userMap =  new ConcurrentHashMap<Long,ConcurrentHashMap<String,ChannelHandlerContext>>();

  /**
   * 用户聊天登录
   */
  public static final String USER_CHAT_LOGIN = "chat_login";

  /**
   * 用户聊天
   */
  public static final String USER_CHAT_CONTENT = "chat_content";

  /**
   * 聊天的topic
   */
  public static final String CHAT = "chat";


  /**
   * 服务器登录。
   */
  public static final String SEVER_LOGIN = "sever_login";



  /**
   * 服务处理程序前缀 + ip 就是服务名称
   */
  public static final String SERVER_PREFIX = "feiyu-server-";

}
