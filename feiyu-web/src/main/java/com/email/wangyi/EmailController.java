package com.email.wangyi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
 
/**
 * @program: springbootemail
 * @Date: 2019/3/1 14:55
 * @Author:xxx
 * @Description:SpringBoot发送邮件
 */
@RestController
@Slf4j
public class EmailController {
    public EmailController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String from;
    
  @GetMapping("/sendMail")
    public String sendMail(String title, int size, String email) {
        // TODO Auto-generated method stub
        SimpleMailMessage message =	new SimpleMailMessage();

      ThreadPools.getThread().execute(new Runnable() {
          @Override
          public void run() {
              for (int i = 0; i <size ; i++) {
                  //发送者
                  message.setFrom(from);
                  //主题
                  message.setSubject("这是第"+i+"条信息");
                  //接收者 15941003631@163.com
                  message.setTo(email);
                  message.setText("我是你的胖鱼呀"+"今天的你是好多鱼"+i);

                  mailSender.send(message);
                  log.info("发送成功 message{} size{}",message,i);
              }
          }
      });
        return "success";
    }



}
