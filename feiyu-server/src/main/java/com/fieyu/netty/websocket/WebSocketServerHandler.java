package com.fieyu.netty.websocket;


import com.alibaba.fastjson.JSON;
import com.feiyu.netty.dto.BaseVO;
import com.feiyu.netty.dto.WsDTO;
import com.fieyu.netty.process.impl.WebSocketProcessImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static com.feiyu.netty.dto.WsDTO.SERVER_PREFIX;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

@Component
@Slf4j
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = Logger.getLogger(WebSocketHandler.class.getName());


    private WebSocketServerHandshaker handshaker;


    private WebSocketProcessImpl webSocketProcess = SpringUtils.getBean(WebSocketProcessImpl.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//传统的HTTP接入
        if (msg instanceof FullHttpRequest) {
            handlerHttpRequest(ctx, (FullHttpRequest) msg);
        }
        //webSocket 接入
        else if (msg instanceof WebSocketFrame) {
            log.info("webSocket success msg{}",msg);
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();  //将消息发送队列中的消息写入到SocketChannel
    }

    //如果HTTP解码失败，返回HTTP异常
    private void handlerHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        if (!req.getDecoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        //构造握手响应返回，本机测试
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://localhost:7008/websocket", null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }


    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        //判断是否是关闭链路的命令
        if (frame instanceof CloseWebSocketFrame) {
            if (((CloseWebSocketFrame) frame).statusCode()==0){
                //如果服务断开链接  断开指定链接
                String request = ((CloseWebSocketFrame) frame).reasonText();
                BaseVO baseVO = JSON.parseObject(request, BaseVO.class);
                log.info("服务器断开链接 ip{}",SERVER_PREFIX+request);
                return;
            }else if(((CloseWebSocketFrame) frame).statusCode()==1){
                String request = ((TextWebSocketFrame) frame).text();
                BaseVO baseVO = JSON.parseObject(request, BaseVO.class);
                WsDTO.clientMap.remove(baseVO.getClientId());
                WsDTO.clientRemoteAddress.remove(baseVO.getClientId());
                ConcurrentHashMap<String, ChannelHandlerContext> map = new ConcurrentHashMap<>();
                map.put(baseVO.getClientId(),ctx);
                WsDTO.userMap.remove(baseVO.getUserId(),new ConcurrentHashMap<String,ChannelHandlerContext>());
                //如果是客户端断开链接 - frame  发送服务处理Web端进行缓存清除
                webSocketProcess.searchOrder(ctx, frame);
                log.info("用户断开链接 ip{}",baseVO.getClientId());
            }

            //关闭链接
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        //判断是否是ping消息
        //        if (frame instanceof PingWebSocketFrame) {
        //            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
        //            return;
        //        }
        //        //本例程仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format("%s frame types not supported ", frame.getClass().
                    getName()));
        }

        try {
//            webSocketProcess.searchOrder(ctx, frame);
            log.info("receive msg msg{}",frame);
        } catch (Exception e) {
            e.printStackTrace();
        }

//		ctx.channel().write(
//				new TextWebSocketFrame(request+", 欢迎使用Netty WebSocket 服务，现在时刻"+new  Date().toString()));
    }


    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        //返回应答给客户端
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            setContentLength(res, res.content().readableBytes());
        }
        //如果是非Keep-Alive， 关闭链接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(res) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //发生异常的时候 调用他
     //   cause.printStackTrace();
        if (WsDTO.serverCtx.contains(ctx.channel())){
            log.info("有得呀 ");
            WsDTO.serverCtx.remove(ctx.channel());
        }

        ctx.close();
    }


}
