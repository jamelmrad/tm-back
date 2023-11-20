package com.telcotek.chatservice.controller;

import com.telcotek.chatservice.models.Message;
import com.telcotek.chatservice.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageController {

    @Autowired
    MessageRepository messageRepository;

    @GetMapping(value = "/chats/{channelId}")
    public List<Message> findMessages(@PathVariable("channelId") String channelId) {
        return messageRepository.findAllByChatId(channelId);
    }
/*
    @PostMapping(value = "/messages")
    public void sendReadReceipt(@RequestBody ReadReceiptRequest request) {
        messageRepository.sendReadReceipt(request.getChannel(), request.getUsername());
    }*/
}
