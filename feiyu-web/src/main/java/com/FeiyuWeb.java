package com;

import com.im.client.ClientByNetty;
import com.common.ThreadPools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.oas.annotations.EnableOpenApi;


@SpringBootApplication
@EnableOpenApi
@EnableDiscoveryClient
public class FeiyuWeb implements CommandLineRunner {

   @Value("${netty.port}")
   private  Integer nettyPort;

    public static void main(String[] args) throws Exception {

        SpringApplication.run(FeiyuWeb.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
            ThreadPools.getThread().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        new ClientByNetty().start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }
}
