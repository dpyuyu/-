package com.im.netty字符串解码;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;


public class TimeClientHandler extends ChannelHandlerAdapter {

   private static final Logger logger =  Logger.getLogger(TimeClientHandler.class.getName());

   private final ByteBuf firstMessage;

   public TimeClientHandler(String str) {
      byte[] req =  str.getBytes();
      firstMessage = Unpooled.buffer(req.length);
      firstMessage.writeBytes(req);
   }

   public TimeClientHandler(ByteBuf firstMessage) {
      this.firstMessage = firstMessage;
   }

   @Override
   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      logger.warn("该客户端已经关闭 ");
      ctx.close();
   }

   @Override
   public void channelActive(ChannelHandlerContext ctx) throws Exception {
      /**
       * 当客户端和服务端链接成功后会调用该方法，发送查询时间的指令给发给服务端 调用ChannelHandlerContext
       * 的writeAndFlush方法将请求信息发送给服务端
       */
      ctx.writeAndFlush(firstMessage);
   }

   @Override
   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      //当服务端返回应答信息 该方法会被调用并打印信息
      String body = (String) msg;
//      byte[] req=new byte[buf.readableBytes()];//创建一个byte数组 并且设置初始字节数
//      buf.readBytes(req); //将缓冲区的字节数组复制到req中
//      String body=new String(req,"UTF-8");//构造函数获取请求信息
      System.out.println("Now is : "+body);
   }
}
