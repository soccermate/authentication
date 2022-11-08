package com.example.authentication.service;

import com.example.authentication.entity.appUser.AppUser;
import com.example.authentication.service.dto.UserCreatedMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@EnableAsync
public class KafkaMessageService implements  MessageService{

    @Value("${spring.kafka.template.default-topic}")
    private String DEFAULT_TOPIC;

    @Autowired
    KafkaTemplate<String, UserCreatedMessage> kafkaTemplate;

    @Async
    @Override
    public CompletableFuture<SendResult<String, UserCreatedMessage>> sendMessage(AppUser appUser){
        return kafkaTemplate.send(DEFAULT_TOPIC, new UserCreatedMessage(appUser)).completable();
    }

    @Override
    public void sendMessageSync(AppUser appUser){
         kafkaTemplate.send(DEFAULT_TOPIC, new UserCreatedMessage(appUser));
    }
}
