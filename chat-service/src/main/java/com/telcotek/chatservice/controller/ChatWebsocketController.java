package com.telcotek.chatservice.controller;

import com.telcotek.chatservice.models.*;
import com.telcotek.chatservice.repository.ChatRepository;
import com.telcotek.chatservice.repository.MessageRepository;
import com.telcotek.chatservice.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
public class ChatWebsocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    ChatService chatService;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ChatRepository chatRepository;

    /**
     * Sends a message to its destination channel
     *
     * @param message
     */
    @MessageMapping("/chats/{chatId}")
    public void handleMessage(Message message, @DestinationVariable("chatId") String chatId) {

        message.setSent_time(LocalDateTime.now());

        Chat chat = chatRepository.findById(chatId).get();
        List<Message> messages = chat.getMessages();
        messages.add(message);
        chat.setMessages(messages);
        messageRepository.save(message);
        chatRepository.save(chat);

        messagingTemplate.convertAndSend("/task-management/chats/" + message.getChatId(), message);
    }
}
