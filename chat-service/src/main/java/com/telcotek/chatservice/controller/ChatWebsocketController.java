package com.telcotek.chatservice.controller;

import com.telcotek.chatservice.models.*;
import com.telcotek.chatservice.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebsocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    ChatService chatService;

    @MessageMapping("/chats/{chatId}")
    @SendTo("/task-management/chats/{chatId}")
    public Message sendMessage(
            @DestinationVariable String chatId,
            @Payload Message message
    ) {
        chatService.send(chatId,message);

        // Broadcast the message content
        messagingTemplate.convertAndSend("/task-management/chats/" + chatId, message);

        return  message;
    }



}
