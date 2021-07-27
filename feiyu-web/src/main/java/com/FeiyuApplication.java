package com;

import com.im.netty.EchoServer;
import com.im.netty的webSocket.WebSocketServer;
import com.thread.ThreadPools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.annotation.PostConstruct;


@SpringBootApplication
public class FeiyuApplication implements CommandLineRunner {

   @Value("${netty.port}")
   private  Integer nettyPort;

    public static void main(String[] args) throws Exception {

        SpringApplication.run(FeiyuApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
            ThreadPools.getThread().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        new WebSocketServer().start(nettyPort);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }
}
