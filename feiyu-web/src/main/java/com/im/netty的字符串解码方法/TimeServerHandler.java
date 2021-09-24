package com.im.netty的字符串解码方法;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.HashMap;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {

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


//		byte[] req=new byte[buf.readableBytes()];//创建一个byte数组 并且设置初始字节数
//		buf.readBytes(req); //将缓冲区的字节数组复制到req中
//		String body=new String(req,"UTF-8");//构造函数获取请求信息
		System.out.println("The time server receive order : "+body);
		String currentTime=
				"QUERY TIME ORDER".equalsIgnoreCase(body)?new java.util.Date(System.currentTimeMillis()).toString():"BAD ORDER";
		ByteBuf resp=Unpooled.copiedBuffer(currentTime.getBytes());


		//ctx.write(Unpooled.copiedBuffer(new String("你好请").getBytes("GBK"))); //异步发送给客户端  将待发送的消息放到缓冲数组中
		ctx.write(Unpooled.copiedBuffer(resp)); //异步发送给客户端  将待发送的消息放到缓冲数组中

		//解析msg    判断msg内信息

	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
		ctx.flush();  //将消息发送队列中的消息写入到SocketChannel
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
		//发生异常的时候 调用他
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
		ctx.close();
	}
}
