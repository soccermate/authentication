package com.example.authentication.service;

import com.example.authentication.entity.appUser.AppUser;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;

public interface MessageService {

    public CompletableFuture<? extends Object> sendMessage(AppUser appUser);

    public void sendMessageSync(AppUser appUser);


}
