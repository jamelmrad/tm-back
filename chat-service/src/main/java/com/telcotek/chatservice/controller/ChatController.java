package com.telcotek.chatservice.controller;

import com.telcotek.chatservice.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    ChatService chatService;

    @PostMapping("/new-chat")
    public void addChat(@RequestParam("mission_id") Long mission_id ,
                        @RequestParam("mission_title") String mission_title) {
        chatService.create(mission_id,mission_title);
    }

}
