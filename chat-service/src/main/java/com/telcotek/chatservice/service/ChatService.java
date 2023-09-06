package com.telcotek.chatservice.service;

import com.telcotek.chatservice.models.Chat;
import com.telcotek.chatservice.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class ChatService {

    @Autowired
    ChatRepository chatRepository;

    public void create(Long mission_id, String mission_title) {
        Chat chat = Chat.builder()
                .mission_id(mission_id)
                .mission_title(mission_title)
                .online_members(0)
                .messages(new ArrayList<>())
                .build();
        chatRepository.save(chat);
    }

    public void delete (String chatId) {
        chatRepository.deleteById(chatId);
    }
}
