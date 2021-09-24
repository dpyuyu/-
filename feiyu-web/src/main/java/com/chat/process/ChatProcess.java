package com.chat.process;

import com.chat.service.IChatService;
import com.common.ChatAnnotation;
import com.common.ICommonSockerProcess;
import com.common.WebSocketException;
import com.feiyu.netty.dto.UserMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class ChatProcess extends ICommonSockerProcess  implements ApplicationContextAware {

    protected  static HashMap<String, IChatService> map = new HashMap<>();

    private static ApplicationContext applicationContext;
    @PostConstruct
    public void init (){
        Map<String, Object> annotation = applicationContext.getBeansWithAnnotation(ChatAnnotation.class);
        Set<Map.Entry<String, Object>> entries = annotation.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            if (entry.getValue() instanceof IChatService){
                IChatService obj = (IChatService)entry.getValue();
                String[] values = obj.getClass().getAnnotation(ChatAnnotation.class).value();
                for (String value : values) {
                    log.info("注入的对象 value{}",value);
                    map.put(value,obj);
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext =applicationContext;
    }

    public static void chat(ChannelHandlerContext ctx, UserMessage userMessage) throws WebSocketException {
        if (userMessage == null) {
            log.info("error chatDTO  chatDTO{}", userMessage);
            throw new WebSocketException("聊天参数异常");
        }
        ChatProcess.map.get(userMessage.getType()).issue(ctx, userMessage);
    }

}
