package com.im.netty;

import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {

   public static final HashMap<ChannelId,ChannelHandlerContext>  map = new HashMap<ChannelId, ChannelHandlerContext>();

	/**
	 * 客户端发送消息会调用他
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
		String body = (String) msg;
		System.out.println("打印：指定字符解码器拼接的数据"+body);
		ctx.write(body);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
		ctx.flush();  //将消息发送队列中的消息写入到SocketChannel
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
		//发生异常的时候 调用他
		cause.printStackTrace();
		ctx.close();
	}


	/**
	 * 有客户端终止服务器链接 会调用该方法
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctx.channel().id();
		super.channelInactive(ctx);
	}
}
