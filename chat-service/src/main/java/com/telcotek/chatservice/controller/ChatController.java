package com.telcotek.chatservice.controller;

import com.telcotek.chatservice.models.Chat;
import com.telcotek.chatservice.models.Message;
import com.telcotek.chatservice.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
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

    @PostMapping("/{id}/send/{team}/{fn}")
    public void send(@PathVariable("id") String id,
                     @PathVariable("team") String teamName,
                     @RequestBody String message,
                     @PathVariable("fn") String fullName) {
        chatService.send(id,message,teamName,fullName);
    }

    @GetMapping("/{chatId}/messages")
    @ResponseBody
    public List<Message> messages(@PathVariable("chatId") String chatId) {
        return chatService.getMessages(chatId);
    }

    @GetMapping("/{id}/from-ids")
    @ResponseBody
    public ResponseEntity<List<Chat>> getAllFromMissionsIds(@PathVariable("id") Long id) {
            List<Chat> chats = chatService.getAll(id);
            return new ResponseEntity<>(chats, HttpStatus.OK);
    }

    @GetMapping("/all")
    @ResponseBody
    public List<Chat> all() {
        List<Chat> chats = chatService.getAllMod();
        return chats;
    }

    @DeleteMapping("/{id}/delete-messages")
    public void deleteAll(@PathVariable("id") String id) {
        chatService.deleteAllMessages(id);
    }

    @GetMapping("/user-chats/{missionIds}")
    @ResponseBody
    public List<Chat> userChats(@PathVariable("missionIds") String missionIds) {
        return chatService.getAllUserChats(missionIds);
    }

}
