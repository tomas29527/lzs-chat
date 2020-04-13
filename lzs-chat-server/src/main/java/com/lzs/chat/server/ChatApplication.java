package com.lzs.chat.server;

import com.lzs.chat.server.server.ChatServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.annotation.Resource;

/**
 * 程序入口
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Slf4j
public class ChatApplication implements CommandLineRunner {
    @Resource(name = "webSocketChatServer")
    private ChatServer webSocketChatServer;


    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            webSocketChatServer.start();
           // Thread.currentThread().join();
        } catch (Exception e) {
            log.error("=====netty 服务启动失败====!", e);
        }
    }
}
