package com.fieyu.task;


import com.feiyu.netty.dto.WsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;
import java.util.Collection;

@Component
@EnableScheduling
@Slf4j
public class HealthExaminationTask {

    @Scheduled(cron = "0/5 * * * * ?")
    public void checkHealthServer(){
        int linkNumebr = WsDTO.serverCtx.size();
        log.info("checkHealthServer linkNumebr:{} address{}",linkNumebr,WsDTO.serverCtx);
    }
}
