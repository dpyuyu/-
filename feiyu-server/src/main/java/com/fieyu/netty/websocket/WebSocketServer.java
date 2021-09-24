package com.fieyu.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WebSocketServer {


    public void start(Integer nettyPort) throws  Exception{
        //配置服务端的NIO线程组
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();//Netty用于服务端的辅助启动类
            b.group(boosGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)//设置积压blacklog 参数为1024
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //编解码工作 和自定义处理类 在这里进行操作
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("http-codec",new HttpServerCodec());//将请求和应答消息编码或者解码为HTTP消息
                            pipeline.addLast("aggergator",new HttpObjectAggregator(65536));//将HTTP消息的多个部分组成为u一个消息
                            pipeline.addLast("http-chunked",new ChunkedWriteHandler());//来向客户端发送HTML5文件 主要支持浏览器端和服务端进行通信
                            pipeline.addLast("handler",new WebSocketServerHandler());// 这个是自定义的WebSocket服务端
                            pipeline.addLast("IdleCheckHandler",new IdleStateHandler(7200,75,9));// 这个是自定义的WebSocket服务端
                        }
                    });//绑定io事件处理类
            //绑定端口，等待同步成功
            Channel f = b.bind(nettyPort).sync().channel();
            log.info("Web Socket server started at nettyPort:"+nettyPort+"_");
            log.info("Open your browser and navigate to http://localhost:"+nettyPort+"/");

            //等待服务端监听端口关闭
            f.closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //优雅的退出，释放宪曾池资源
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }



    /**
     * @param args
     * @throws Exception
     */
    public  static void main(String[] args) throws Exception {
        int port = 7008;
        if (args!=null&&args.length>0){
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        new WebSocketServer().start(port);
    }


}
