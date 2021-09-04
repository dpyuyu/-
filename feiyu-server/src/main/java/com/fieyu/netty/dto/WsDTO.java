package com.fieyu.netty.dto;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class WsDTO  {
  public static int LOAD_BALANCING  = 0;

  public static  ConcurrentHashMap<String ,ChannelHandlerContext> clientMap =  new ConcurrentHashMap<String, ChannelHandlerContext>();
  public static  ConcurrentHashMap<String, SocketAddress> clientRemoteAddress =  new ConcurrentHashMap<String,SocketAddress>();
  public static  ConcurrentHashMap<Long,ConcurrentHashMap<String,ChannelHandlerContext>> userMap =  new ConcurrentHashMap<Long,ConcurrentHashMap<String,ChannelHandlerContext>>();



  //服务器ctx集合
  public static List<Channel> serverCtx =  new ArrayList<>();

  /**
   * 用户聊天登录
   */
  public static final String USER_MSG = "user_msg";

  /**
   * 用户聊天登录
   */
  public static final String USER_CHAT_LOGOUT = "chat_login_logout";

  /**
   * 服务器登录。
   */
  public static final String SEVER_LOGIN = "sever_login";

  /**
   * 业务处理程序服务器-想mq发送小题Topic
   */
  public static final String SEVER_MSG = "sever_msg";

  /**
   * 服务处理程序前缀 + ip 就是服务名称
   */
  public static final String SERVER_PREFIX = "feiyu-server-";

}
