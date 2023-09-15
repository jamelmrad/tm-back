package com.telcotek.chatservice.controller;

import com.telcotek.chatservice.models.Chat;
import com.telcotek.chatservice.models.Message;
import com.telcotek.chatservice.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{chatId}/messages")
    @ResponseBody
    public List<Message> messages(@PathVariable("chatId") String chatId) {
        return chatService.getMessages(chatId);
    }

    @GetMapping("/from-ids")
    @ResponseBody
    public ResponseEntity<List<Chat>> getAllFromMissionsIds(@RequestParam("email") String email) {
        try {
            List<Chat> chats = chatService.retrieveAllFromMissionIds(email);

            return new ResponseEntity<>(chats, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
